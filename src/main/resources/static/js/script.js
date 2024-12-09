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

// 카드 컨테이너
const plannerCardContainer = document.getElementById("plannerCardContainer");

// 카드 추가 함수
function addPlannerCard(plannerData) {
    console.log("새로운 플래너 카드 추가:", plannerData);

    // 카드 생성
    const card = document.createElement("div");
    card.className = "planner-card";
    card.dataset.id = plannerData.id;

    // 카드 내용
    card.innerHTML = `
        <h3>${plannerData.title || `Tour ${plannerData.id}`}</h3>
        <p>${plannerData.description || `작성일: ${plannerData.createdDate}`}</p>
        <button onclick="viewPlanner(${plannerData.id})">보기</button>
    `;

    // 카드 컨테이너에 추가
    plannerCardContainer.appendChild(card);
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
