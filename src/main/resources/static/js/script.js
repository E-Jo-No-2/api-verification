

// DOMContentLoaded 이벤트: 날씨 데이터 및 플래너 목록 불러오기
document.addEventListener("DOMContentLoaded", function () {
    // 플래너 목록 불러오기
    fetch('/api/planner/list?userId=testuser') // userId는 실제 사용자 ID로 변경
        .then((response) => {
            if (!response.ok) {
                throw new Error("플래너 목록 불러오기 실패");
            }
            return response.json();
        })
        .then((data) => {
            console.log("플래너 목록:", data);
            const plannerCardContainer = document.querySelector('#plannerCardContainer');
            plannerCardContainer.innerHTML = ""; // 기존 내용 초기화

            data.forEach((planner) => {
                const plannerCard = document.createElement('div');
                plannerCard.classList.add('planner-card');
                plannerCard.dataset.id = planner.plannerId;

                plannerCard.innerHTML = `
                    <h3>Tour ${planner.plannerId}</h3>
                    <p>Date: ${planner.date}</p>
                    <button onclick="viewPlanner(${planner.plannerId})">Planner 보기</button>
                    <button onclick="deletePlannerCard(${planner.plannerId})">Planner 삭제</button>
                `;

                plannerCardContainer.appendChild(plannerCard);
            });
        })
        .catch((error) => {
            console.error("플래너 목록 불러오기 실패:", error);
        });

    // 날씨 데이터 불러오기
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
                    <div class="weather-forecast" data-date="${date.toISOString().slice(0, 10)}">
                        <div class="weather-icon" style="cursor: pointer;">
                            <img src="${iconUrl}" alt="Weather Icon">
                        </div>
                        <h3>${monthDay}</h3>
                        <p>${description}</p>
                        <p>${temp}</p>
                    </div>
                `;
            });

            const weatherDataElement = document.getElementById("weather-data");
            weatherDataElement.innerHTML = weatherHtml;

            // 날씨 아이콘 클릭 이벤트 등록
            const weatherForecasts = document.querySelectorAll(".weather-forecast");
            weatherForecasts.forEach((forecastElement) => {
                forecastElement.querySelector(".weather-icon").addEventListener("click", () => {
                    const selectedDate = forecastElement.dataset.date;
                    createPlanner(selectedDate);
                });
            });
        })
        .catch((error) => {
            console.error("날씨 데이터를 불러오지 못했습니다:", error);
            document.getElementById("weather-data").innerText = "날씨 데이터를 가져오지 못했습니다.";
        });
});

// 플래너 생성 및 카드 업데이트
function createPlanner(date) {
    const createButton = document.querySelector(`#create-planner-button-${date}`); // 버튼 선택
    if (createButton) {
        createButton.disabled = true; // 버튼 비활성화
    }

    fetch('/api/planner/save', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ userId: 'testuser', date: date }) // userId는 실제 사용자 ID로 교체 필요
    })
        .then((response) => {
            if (!response.ok) {
                throw new Error("플래너 생성 실패");
            }
            return response.json();
        })
        .then((data) => {
            console.log('Planner 생성 성공:', data);
            if (data.plannerId) {
                updatePlannerCard(data.plannerId, date);
                alert(`날짜 ${date}에 대한 플래너가 생성되었습니다.`);
                window.location.href = `/landmark?plannerId=${data.plannerId}&userId=testuser`;
            }
        })
        .catch((error) => {
            console.error('Planner 생성 실패:', error);
            alert("플래너 생성 중 오류가 발생했습니다.");
        })
        .finally(() => {
            if (createButton) {
                createButton.disabled = false; // 요청 완료 후 버튼 활성화
            }
        });
}

// 플래너 카드 추가
function updatePlannerCard(plannerId, date) {
    const plannerCard = document.createElement('div');
    plannerCard.classList.add('planner-card');
    plannerCard.dataset.id = plannerId;

    plannerCard.innerHTML = `
        <h3>Planner ID: ${plannerId}</h3>
        <p>Date: ${date}</p>
        <button onclick="viewPlanner(${plannerId})">Planner 보기</button>
        <button onclick="deletePlannerCard(${plannerId})">Planner 삭제</button>
    `;

    document.querySelector('#plannerCardContainer').appendChild(plannerCard);
}

// 플래너 보기 버튼
function viewPlanner(plannerId) {
    alert(`플래너 ${plannerId}을(를) 엽니다.`);
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

    fetch(`/api/planner/delete?plannerId=${plannerId}`, {
        method: "DELETE",
    })
        .then((response) => {
            if (!response.ok) {
                throw new Error("플래너 삭제 실패");
            }
            return response.text();
        })
        .then(() => {
            const card = document.querySelector(`.planner-card[data-id="${plannerId}"]`);
            if (card) {
                card.remove();
                alert(`플래너 ${plannerId}이(가) 성공적으로 삭제되었습니다.`);
            }
        })
        .catch((error) => {
            console.error("플래너 삭제 중 오류 발생:", error);
            alert("플래너 삭제 실패");
        });
}





