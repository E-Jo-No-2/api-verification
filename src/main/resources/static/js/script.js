// script.js

// DOM 요소 가져오기
const cardsContainer = document.getElementById("cards");
const newPlannerBtn = document.getElementById("new-planner-btn");

// 플래너 카운트 (임의로 설정)
let plannerCount = 0;

// userId를 설정
const userId = "frodo1234"; // 실제 userId 값으로 설정

// NEW 버튼 클릭 이벤트
newPlannerBtn.addEventListener("click", () => {
    // 새로운 플래너 생성
    plannerCount++;
    const newCard = document.createElement("a");
    newCard.href = `/landmark?plannerId=${plannerCount}&userId=${userId}`;
    newCard.className = "card";
    newCard.innerHTML = `
        <h3>CATEGORY</h3>
        <h2>TOUR${plannerCount}</h2>
        <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>
    `;
    cardsContainer.insertBefore(newCard, newPlannerBtn);
});

// WeatherApp API 호출
document.addEventListener("DOMContentLoaded", function() {
    fetch('/getWeather')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.json();
        })
        .then(data => {
            if (data.error) {
                throw new Error(data.error);
            }

            const forecasts = data.list; // 필터링된 5일간의 오전 12시 날씨 데이터

            let weatherHtml = "";
            forecasts.forEach(forecast => {
                const date = new Date(forecast.dt * 1000);
                const monthDay = `${date.getMonth() + 1}월 ${date.getDate()}일}`;
                const iconCode = forecast.weather[0].icon;
                const iconUrl = `https://openweathermap.org/img/wn/${iconCode}.png`;
                const temp = `${Math.round(forecast.main.temp)}°C`; // 온도 반올림

                weatherHtml += `
                    <div class="weather-forecast">
                        <div class="weather-icon">
                            <img src="${iconUrl}" alt="Weather Icon">
                        </div>
                        <h3>${monthDay}</h3>
                        <p>${temp}</p>
                    </div>
                `;
            });

            document.getElementById("weather-data").innerHTML = weatherHtml;
        })
        .catch(error => {
            console.error('Error during fetch request:', error);
            document.getElementById("weather-data").innerText = error.message;
        });
});
