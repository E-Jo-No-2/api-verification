document.addEventListener("DOMContentLoaded", function () {
    const map = new naver.maps.Map('map', {
        center: new naver.maps.LatLng(37.5665, 126.9780),
        zoom: 10
    });

    let selectedOption = "trafast"; // 기본 옵션 설정
    const plannerId = getPlannerIdFromUrl(); // URL에서 plannerId를 추출하는 함수

    // URL에서 plannerId 확인
    if (!plannerId) {
        alert("plannerId가 설정되지 않았습니다!");
        throw new Error("plannerId가 필요합니다.");
    }
    console.log("Planner ID:", plannerId);

    // 메모 불러오기 함수 호출
    loadMemo(plannerId);

    // 투어 리스트 불러오기 함수 호출
    loadTourList(plannerId);

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
            body: new URLSearchParams({ start, goal, option: selectedOption, plannerId }) // plannerId 추가
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

    // My Tour List 불러오기 함수
    function loadTourList(plannerId) {
        const uri = `/api/planner-spot/list/${plannerId}`; // URI 생성
        console.log("투어 리스트 불러오기 요청 URI:", uri);

        fetch(uri, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
            .then(response => {
                console.log("투어 리스트 응답 상태:", response.status);
                if (!response.ok) {
                    throw new Error(`투어 리스트 불러오기 실패: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                console.log("투어 리스트 불러오기 성공:", data);

                // 불러온 데이터를 #tourList에 추가
                const tourList = document.getElementById("tourList");
                tourList.innerHTML = ''; // 기존 내용 초기화

                data.forEach((spot, index) => {
                    const listItem = document.createElement("li");
                    listItem.textContent = `${index + 1}. ${spot.spotName}`; // 장소 이름만 표시
                    tourList.appendChild(listItem);
                });

                console.log("투어 리스트 렌더링 완료");
            })
            .catch(error => {
                console.error("투어 리스트 불러오기 실패:", error.message);
                alert("투어 리스트를 불러오지 못했습니다.");
            });
    }

    // Memo 불러오기
    function loadMemo(plannerId) {
        console.log("loadMemo 함수 실행: plannerId =", plannerId); // 함수 내부 로그 추가
        const uri = `/memos/planner/${plannerId}`; // URI 생성
        console.log("메모 불러오기 요청 URI:", uri);

        fetch(uri, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
            .then(response => {
                console.log("메모 불러오기 응답 상태:", response.status);
                if (!response.ok) {
                    throw new Error(`메모 불러오기 실패: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                console.log("메모 불러오기 성공:", data);
                const memoId = data[0]?.memoId;
                console.log("메모 ID:", memoId);

                // 메모를 화면의 textarea에 표시
                document.getElementById("memo").value = data[0]?.memoContent || ''; // 첫 번째 메모 표시

                // 버튼 ID 수정
                const saveMemoBtn = document.getElementById("saveMemoBtn");
                if (saveMemoBtn) {
                    saveMemoBtn.addEventListener("click", function () {
                        const memoContent = document.getElementById("memo").value.trim();
                        updateMemo(memoId, memoContent);
                    });
                    console.log("saveMemoBtn 이벤트 리스너 추가 완료");
                } else {
                    console.error("saveMemoBtn이 DOM에 존재하지 않습니다.");
                }
            })
            .catch(error => {
                console.error("메모 불러오기 실패:", error.message);
                alert("메모를 불러오지 못했습니다.");
            });
    }

    // Memo 업데이트
    function updateMemo(memoId, memoContent) {
        fetch(`/memos/${memoId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ memoContent })
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`메모 업데이트 실패: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                console.log("메모 업데이트 성공:", data);
                alert("메모가 저장되었습니다.");
            })
            .catch(error => {
                console.error("메모 업데이트 실패:", error.message);
                alert("메모를 저장하는 중 문제가 발생했습니다.");
            });
    }

    function getPlannerIdFromUrl() {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get('plannerId');
    }
});

// '처음으로' 버튼 클릭 이벤트 추가
const backToHomeBtn = document.getElementById("backToHome");
backToHomeBtn.addEventListener("click", function () {
    if (confirm("처음으로 돌아가시겠습니까?")) {
        window.location.href = "/"; // 처음으로 이동할 경로
    }
});
