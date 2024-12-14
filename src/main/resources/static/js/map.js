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

    // 메모 및 투어 리스트 불러오기
    loadMemo(plannerId);
    loadTourList(plannerId);

    // 경로 데이터 가져오기
    fetch(`/get-routes?plannerId=${plannerId}`)
        .then(response => response.json())
        .then(routes => {
            if (routes.length > 0) {
                const start = `${routes[0].start_point.lng},${routes[0].start_point.lat}`;
                const goals = routes.map(route => `${route.end_point.lng},${route.end_point.lat}`).join('|');

                document.getElementById("start").value = start;
                document.getElementById("goal").value = goals;

                // 폼 자동 제출
                document.getElementById("routeForm").dispatchEvent(new Event('submit'));
            }
        })
        .catch(error => console.error('Error:', error));

    // 투어 리스트 불러오기
    function loadTourList(plannerId) {
        const uri = `/api/planner-spot/list/${plannerId}`;
        console.log("투어 리스트 불러오기 요청 URI:", uri);

        fetch(uri, {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        })
            .then(response => response.ok ? response.json() : Promise.reject(`Error ${response.status}`))
            .then(data => {
                const tourList = document.getElementById("tourList");
                tourList.innerHTML = '';
                data.forEach((spot, index) => {
                    const listItem = document.createElement("li");
                    listItem.textContent = `${index + 1}. ${spot.spotName}`;
                    tourList.appendChild(listItem);
                });
                console.log("투어 리스트 렌더링 완료");
            })
            .catch(error => {
                console.error("투어 리스트 불러오기 실패:", error);
                alert("투어 리스트를 불러오지 못했습니다.");
            });
    }

    // 메모 불러오기
    function loadMemo(plannerId) {
        const uri = `/memos/planner/${plannerId}`;
        console.log("메모 불러오기 요청 URI:", uri);

        fetch(uri, {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        })
            .then(response => response.ok ? response.json() : Promise.reject(`Error ${response.status}`))
            .then(data => {
                const memoId = data[0]?.memoId;
                document.getElementById("memo").value = data[0]?.memoContent || '';

                const saveMemoBtn = document.getElementById("saveMemoBtn");
                saveMemoBtn?.addEventListener("click", function () {
                    const memoContent = document.getElementById("memo").value.trim();
                    updateMemo(memoId, memoContent);
                });
            })
            .catch(error => {
                console.error("메모 불러오기 실패:", error);
                alert("메모를 불러오지 못했습니다.");
            });
    }

    // 옵션 버튼 클릭 이벤트
    document.getElementById("options-buttons")?.addEventListener("click", (event) => {
        if (event.target.tagName === "BUTTON") {
            selectedOption = event.target.getAttribute("data-option");
            document.querySelectorAll("#options-buttons button").forEach(btn => btn.style.backgroundColor = "");
            event.target.style.backgroundColor = "#d3d3d3";
        }
    });

    // 경로 표시
    function addMarkers(startCoords, goalCoords) {
        new naver.maps.Marker({
            position: new naver.maps.LatLng(startCoords[1], startCoords[0]),
            map: map,
            title: "출발지"
        });
        new naver.maps.Marker({
            position: new naver.maps.LatLng(goalCoords[1], goalCoords[0]),
            map: map,
            title: "목적지"
        });
    }

    function drawRoute(routeCoordinates, startCoords, goalCoords, sections) {
        const usedIndices = new Set();
        sections.forEach(section => {
            const sectionPath = routeCoordinates
                .slice(section.pointIndex, section.pointIndex + section.pointCount)
                .map(coord => new naver.maps.LatLng(coord[1], coord[0]));
            let color = '#00FF00';
            if (section.congestion === 2) color = '#FFFF00';
            else if (section.congestion === 3) color = '#FF0000';

            new naver.maps.Polyline({
                map: map,
                path: sectionPath,
                strokeColor: color,
                strokeWeight: 5
            });
            for (let i = section.pointIndex; i < section.pointIndex + section.pointCount; i++) {
                usedIndices.add(i);
            }
        });

        focusOnRoute([startCoords, ...routeCoordinates, goalCoords]);
    }

    function focusOnRoute(routeCoordinates) {
        const bounds = new naver.maps.LatLngBounds();
        routeCoordinates.forEach(coord => bounds.extend(new naver.maps.LatLng(coord[1], coord[0])));
        map.fitBounds(bounds);
    }

    document.getElementById("routeForm")?.addEventListener("submit", function (e) {
        e.preventDefault();
        const start = document.getElementById("start").value.trim();
        const goal = document.getElementById("goal").value.trim();
        const startCoords = start.split(',').map(Number);
        const goalCoords = goal.split(',').map(Number);

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
                drawRoute(routeCoordinates, startCoords, goalCoords, sections);
            })
            .catch(error => console.error('Error:', error));
    });

    document.getElementById("backToHome")?.addEventListener("click", function () {
        if (confirm("처음으로 돌아가시겠습니까?")) {
            window.location.href = "/";
        }
    });

    function getPlannerIdFromUrl() {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get('plannerId');
    }

    function updateMemo(memoId, memoContent) {
        fetch(`/update-memo/${memoId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ memoContent })
        })
            .then(response => response.ok ? alert("메모가 저장되었습니다.") : Promise.reject("Error updating memo"))
            .catch(error => alert("메모 저장 실패: " + error));
    }
});

document.addEventListener("DOMContentLoaded", function () {
    // 기존 코드...

    // '처음으로' 버튼 클릭 이벤트 추가
    const backToHomeBtn = document.getElementById("backToHome");
    backToHomeBtn.addEventListener("click", function () {
        if (confirm("처음으로 돌아가시겠습니까?")) {
            window.location.href = "/"; // 처음으로 이동할 경로
        }
    });
});
