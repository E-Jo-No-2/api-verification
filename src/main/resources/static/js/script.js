// Weather API 호출하기
document.addEventListener("DOMContentLoaded", function () {
    fetch("/getWeather")
        .then((response) => {
            if (!response.ok) {
                throw new Error("날씨 데이터를 불러오는 데 실패했습니다: " + response.statusText);
            }
            return response.json();
        })
        .then((data) => {
            console.log("날씨 데이터:", data);
            if (data.error) {
                throw new Error(data.error);
            }

            const forecasts = data.list || [];
            let weatherHtml = "";
            forecasts.forEach((forecast) => {
                const date = new Date(forecast.dt * 1000);
                const monthDay = `${date.getMonth() + 1}월 ${date.getDate()}일`;
                const iconCode = forecast.weather[0].icon;
                const iconUrl = `https://openweathermap.org/img/wn/${iconCode}.png`;
                const description = forecast.weather[0].description;
                const temp = `${Math.round(forecast.main.temp)}°C`;

                weatherHtml += `
                    <div class="weather-forecast">
                        <div class="weather-icon">
                            <img src="${iconUrl}" alt="Weather Icon">
                        </div>
                        <h3>${monthDay}</h3>
                        <p>${description}</p>
                        <p>${temp}</p>
                    </div>
                `;
            });

            document.getElementById("weather-data").innerHTML = weatherHtml;
        })
        .catch((error) => {
            console.error("날씨 데이터를 불러오지 못했습니다:", error);
            document.getElementById("weather-data").innerText = "날씨 데이터를 가져오지 못했습니다.";
        });
});

//document.addEventListener("DOMContentLoaded", function () {
const plannerCardContainer = document.getElementById("plannerCardContainer");
// 서버에서 플래너 목록을 받아옵니다.
fetch("/api/planner/list?userId=testuser")  // userId는 실제 값으로 변경
    .then((response) => {
        if (!response.ok) {
            throw new Error("플래너 목록을 불러오는 데 실패했습니다.");
        }
        return response.json();
    })
    .then((planners) => {
        console.log("플래너 목록:", planners);
        planners.forEach((planner) => {
            addPlannerCard(planner);
        });
    })
    .catch((error) => {
        console.error("플래너 목록 불러오기 오류:", error);
    });
//});

// 카드 컨테이너


// 카드 추가 함수
function addPlannerCard(plannerData) {
    console.log("새로운 플래너 카드 추가:", plannerData);

    // 카드 생성
    const card = document.createElement("div");
    card.className = "planner-card";
    card.dataset.id = plannerData.plannerId; // 정확한 ID 설정

    // 날짜 입력 필드 추가
    const dateInput = `<input type="date" id="planner-date-${plannerData.plannerId}" value="${plannerData.date || ''}">`;

    // 카드 내용
    card.innerHTML = `
         <h3>Planner ${plannerData.plannerId}</h3>
         ${dateInput}
        <button onclick="viewPlanner(${plannerData.plannerId})"> Planner 보기</button>
        <button onclick="deletePlannerCard(${plannerData.plannerId})">Planner 삭제</button>
          <button onclick="updatePlannerDate(${plannerData.plannerId})">날짜 업데이트</button>
    \
    `;

    // 카드 컨테이너에 추가
    plannerCardContainer.appendChild(card);
}

// 날짜 업데이트 함수
function updatePlannerDate(plannerId) {
    const dateInput = document.getElementById(`planner-date-${plannerId}`);
    const newDate = dateInput.value;

    if (!newDate) {
        alert("날짜를 입력해 주세요.");
        return;
    }

    console.log(`플래너 ${plannerId}의 날짜를 ${newDate}로 업데이트`);

    fetch(`/api/planner/update?plannerId=${plannerId}&userId=testuser&newDate=${newDate}`, {
        method: "PUT",
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("플래너 날짜 업데이트 실패");
            }
            return response.text();
        })
        .then(() => {
            alert(`플래너 ${plannerId}의 날짜가 ${newDate}로 업데이트되었습니다.`);
        })
        .catch(error => {
            console.error("날짜 업데이트 중 오류 발생:", error);
            alert("날짜 업데이트 실패");
        });
}


// 커스텀 이벤트 리스너 등록
window.addEventListener("addPlannerCard", (event) => {
    const plannerData = event.detail;
    addPlannerCard(plannerData);
});

// 플래너 보기 버튼
function viewPlanner(plannerId) {
    alert(`플래너 ${plannerId}을(를) 엽니다.`);
    // 원하는 동작 구현
    window.location.href = `/planner/view?plannerId=${plannerId}`;
}

// 플래너 카드 삭제
function deletePlannerCard(plannerId) {
    console.log(`플래너 카드 삭제 요청: ${plannerId}`);

    if (!plannerId) {
        console.error("plannerId가 유효하지 않습니다.");
        return;
    }

    if (!confirm(`플래너 ${plannerId}을(를) 삭제하시겠습니까?`)) {
        console.log("삭제 작업 취소");
        return;
    }

    console.log(`플래너 삭제 API 호출: ${plannerId}`);
    fetch(`/api/planner/delete?plannerId=${plannerId}`, {  // URL을 올바르게 수정
        method: "DELETE",
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("플래너 삭제 실패");
            }
            return response.text();
        })
        .then(() => {
            // DOM에서 카드 삭제
            const card = document.querySelector(`.planner-card[data-id="${plannerId}"]`);
            if (card) {
                card.remove();
                console.log(`플래너 카드 ${plannerId} 삭제 성공`);
                alert(`플래너 ${plannerId}이(가) 성공적으로 삭제되었습니다.`);
            } else {
                console.warn(`플래너 카드 ${plannerId}를 찾을 수 없습니다.`);
            }
        })
        .catch(error => {
            console.error("플래너 삭제 중 오류 발생:", error);
            alert("플래너 삭제 실패");
        });
}


// 플래너 목록 새로 고침
function reloadPlannerList() {
    fetch("/api/planner/list?userId=testuser")  // userId는 실제 값으로 변경
        .then(response => {
            if (!response.ok) {
                throw new Error("플래너 목록을 불러오는 데 실패했습니다.");
            }
            return response.json();
        })
        .then((planners) => {
            plannerCardContainer.innerHTML = '';  // 기존 카드들 초기화
            planners.forEach((planner) => {
                addPlannerCard(planner);
            });
        })
        .catch((error) => {
            console.error("플래너 목록 불러오기 오류:", error);
        });
}

// 플래너 추가 버튼 클릭 이벤트
const newPlannerBtn = document.getElementById("new-planner-btn");

newPlannerBtn.addEventListener("click", () => {
    console.log("new 버튼 클릭");
    fetch("/api/planner/save", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: new URLSearchParams({
            userId: "testuser"  // 실제 사용자 ID로 교체 필요
        })
    })
        .then(response => {
            console.log("서버 응답:", response);
            return response.json();
        })
        .then(data => {
            console.log("응답 데이터:", data);
            if (data.plannerId) {
                // 이벤트 트리거로 카드 추가
                const event = new CustomEvent("addPlannerCard", {
                    detail: {
                        id: data.plannerId,
                        title: `Tour ${data.plannerId}`,
                        createdDate: new Date().toISOString().slice(0, 10)
                    }
                });
                window.dispatchEvent(event);

                // SelectLandmark 페이지로 이동
                window.location.href = `/landmark?plannerId=${data.plannerId}&userId=testuser`;
            } else {
                console.log("페이지 이동 실패");
            }
        })
        .catch(error => {
            console.error("플래너 저장 중 오류 발생:", error);
            alert("플래너 저장 중 오류가 발생했습니다.");
        });
});
