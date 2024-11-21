// script.js

// DOM 요소 가져오기
const cardsContainer = document.getElementById("cards");
const newPlannerBtn = document.getElementById("new-planner-btn");

// 플래너 카운트 (임의로 설정)
let plannerCount = 0;

// NEW 버튼 클릭 이벤트
newPlannerBtn.addEventListener("click", () => {
    // 새로운 플래너 생성
    plannerCount++;
    const newCard = document.createElement("a");
    newCard.href = `planner${plannerCount}.html`;
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
    fetch('/getWeather?city=Seoul')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.json();
        })
        .then(data => {
            const iconCode = data.weather[0].icon;
            const iconUrl = `https://openweathermap.org/img/wn/${iconCode}@2x.png`;

            document.getElementById("weather-data").innerHTML = `
                <div class="weather-icon">
                    <img src="${iconUrl}" alt="Weather Icon">
                </div>
                <div class="weather-details">
                    <h3>${data.name}의 날씨:</h3>
                    <p>${data.weather[0].description}</p>
                    <p>현재 온도: ${data.main.temp}°C</p>
                </div>
            `;
        })
        .catch(error => {
            console.error('Error during fetch request:', error);
            document.getElementById("weather-data").innerText = "날씨 정보를 불러오는 도중 오류가 발생했습니다.";
        });
});
