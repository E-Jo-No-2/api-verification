document.addEventListener("DOMContentLoaded", function () {
    const map = new naver.maps.Map('map', {
        center: new naver.maps.LatLng(37.5665, 126.9780),
        zoom: 10
    });

    let selectedOption = "trafast"; // 기본 옵션 설정
    const plannerId = getPlannerIdFromUrl(); // URL에서 plannerId를 추출하는 함수

    if (plannerId) {
        // 경로 데이터를 가져와 입력 필드에 채우기
        fetch(`/get-routes?plannerId=${plannerId}`)
            .then(response => response.json())
            .then(routes => {
                if (routes.length > 0) {
                    const start = `${routes[0].start_point.lng},${routes[0].start_point.lat}`;
                    const goals = routes.map(route => `${route.end_point.lng},${route.end_point.lat}`).join('|');

                    document.getElementById("start").value = start;
                    document.getElementById("goal").value = goals;

                    // 폼을 자동으로 제출
                    document.getElementById("routeForm").dispatchEvent(new Event('submit'));
                }
            })
            .catch(error => console.error('Error:', error));
    } else {
        console.error('Planner ID not found in URL');
    }

    // URL에서 plannerId를 추출하는 함수 (예시)
    function getPlannerIdFromUrl() {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get('plannerId');
    }

    // 옵션 버튼 클릭 이벤트 추가
    document.getElementById("options-buttons").addEventListener("click", (event) => {
        if (event.target.tagName === "BUTTON") {
            selectedOption = event.target.getAttribute("data-option");
            document.querySelectorAll("#options-buttons button").forEach(btn => btn.style.backgroundColor = ""); // 버튼 색상 초기화
            event.target.style.backgroundColor = "#d3d3d3"; // 선택된 버튼 강조
        }
    });

    // 출발지 및 목적지에 마커 추가
    function addMarkers(startCoords, goalCoords) {
        new naver.maps.Marker({
            position: new naver.maps.LatLng(startCoords[1], startCoords[0]),
            map: map,
            title: "출발지"
        });

        goalCoords.forEach(coord => {
            new naver.maps.Marker({
                position: new naver.maps.LatLng(coord[1], coord[0]),
                map: map,
                title: "목적지"
            });
        });
    }

    // 경로 그리기
    function drawRoute(routeCoordinates, startCoords, goalCoords, sections) {
        const usedIndices = new Set();

        // 혼잡도 구간 그리기
        sections.forEach(section => {
            const sectionPath = routeCoordinates
                .slice(section.pointIndex, section.pointIndex + section.pointCount)
                .map(coord => new naver.maps.LatLng(coord[1], coord[0]));

            let color = '#00FF00'; // 기본 색상 (원활)
            if (section.congestion === 2) color = '#FFFF00'; // 보통
            else if (section.congestion === 3) color = '#FF0000'; // 혼잡

            new naver.maps.Polyline({
                map: map,
                path: sectionPath,
                strokeColor: color,
                strokeWeight: 5
            });

            // 사용된 인덱스 저장
            for (let i = section.pointIndex; i < section.pointIndex + section.pointCount; i++) {
                usedIndices.add(i);
            }
        });

        // 정보가 없는 구간 그리기
        let startIndex = null;
        for (let i = 0; i < routeCoordinates.length; i++) {
            if (!usedIndices.has(i)) {
                if (startIndex === null) startIndex = i;
            } else if (startIndex !== null) {
                const unknownPath = routeCoordinates
                    .slice(startIndex, i)
                    .map(coord => new naver.maps.LatLng(coord[1], coord[0]));

                new naver.maps.Polyline({
                    map: map,
                    path: unknownPath,
                    strokeColor: '#00FF00', // 기본 색상 (초록색)
                    strokeWeight: 5
                });

                startIndex = null;
            }
        }

        // 마지막 처리
        if (startIndex !== null) {
            const unknownPath = routeCoordinates
                .slice(startIndex)
                .map(coord => new naver.maps.LatLng(coord[1], coord[0]));

            new naver.maps.Polyline({
                map: map,
                path: unknownPath,
                strokeColor: '#00FF00', // 기본 색상 (초록색)
                strokeWeight: 5
            });
        }

        // 출발지와 도착지 점선 연결
        if (routeCoordinates.length > 0) {
            new naver.maps.Polyline({
                map: map,
                path: [
                    new naver.maps.LatLng(startCoords[1], startCoords[0]),
                    new naver.maps.LatLng(routeCoordinates[0][1], routeCoordinates[0][0])
                ],
                strokeColor: '#7b7b7b',
                strokeWeight: 3,
                strokeLineDash: [10, 10],
                strokeOpacity: 0.8
            });

            new naver.maps.Polyline({
                map: map,
                path: [
                    new naver.maps.LatLng(routeCoordinates[routeCoordinates.length - 1][1], routeCoordinates[routeCoordinates.length - 1][0]),
                    new naver.maps.LatLng(goalCoords[1], goalCoords[0])
                ],
                strokeColor: '#7b7b7b',
                strokeWeight: 3,
                strokeLineDash: [10, 10],
                strokeOpacity: 0.8
            });
        }

        focusOnRoute([startCoords, ...routeCoordinates, ...goalCoords]);
    }

    // 경로 중심으로 지도 맞춤
    function focusOnRoute(routeCoordinates) {
        const bounds = new naver.maps.LatLngBounds();
        routeCoordinates.forEach(coord => bounds.extend(new naver.maps.LatLng(coord[1], coord[0])));
        map.fitBounds(bounds);
    }

    // 경로 정보 표시
    function displayRouteInfo(routeData, selectedOption) {
        const routeDisplayDiv = document.getElementById("routeDisplay");
        if (!routeDisplayDiv) {
            console.error("routeDisplayDiv element not found");
            return;
        }

        routeDisplayDiv.innerHTML = ''; // 이전 내용 지우기

        const summary = routeData[selectedOption]?.summary || {};

        routeDisplayDiv.innerHTML = `
            <div class="option-box">
                <h3>${selectedOption}</h3>
                <p><b>거리:</b> ${summary.distance || 'N/A'}m</p>
                <p><b>소요 시간:</b> ${Math.ceil((summary.duration || 0) / 60000)}분</p>
                <p><b>택시 요금:</b> ${summary.taxiFare || 'N/A'}원</p>
            </div>
        `;
    }

    document.getElementById("routeForm").addEventListener("submit", function (e) {
        e.preventDefault();

        const start = document.getElementById("start").value.trim();
        const goal = document.getElementById("goal").value.trim();
        const startCoords = start.split(',').map(Number);
        const goalCoords = goal.split('|').map(point => point.split(',').map(Number));

        addMarkers(startCoords, goalCoords);

        fetch('/calculate-route', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: new URLSearchParams({ start, goal, option: selectedOption })
        })
            .then(response => response.json())
            .then(routeData => {
                const routeCoordinates = routeData[selectedOption]?.path || [];
                const sections = routeData[selectedOption]?.section || [];
                drawRoute(routeCoordinates, startCoords, goalCoords, sections); // 경로와 점선 표시
                displayRouteInfo(routeData, selectedOption); // 경로 정보를 표시
            })
            .catch(error => console.error('Error:', error));
    });

    function getPlannerIdFromUrl() {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get('plannerId');
    }
});
