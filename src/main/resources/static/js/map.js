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

    // 투어 리스트 불러오기 함수
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

    function getPlannerIdFromUrl() {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get('plannerId');
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
