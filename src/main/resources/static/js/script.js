// script.js

// DOM 요소 가져오기
const cardsContainer = document.getElementById("cards");
const newPlannerBtn = document.getElementById("new-planner-btn");

// 플래너 카운트 (임의로 설정)
let plannerCount = 0;

// userId를 설정
const userId = "testuser"; // 실제 userId 값으로 설정

// NEW 버튼 클릭 이벤트
newPlannerBtn.addEventListener("click", () => {
    // 새로운 플래너 생성
    plannerCount++;
    const date = new Date().toISOString().split('T')[0]; // 현재 날짜를 'YYYY-MM-DD' 형식으로 가져오기
    const newCard = document.createElement("a");
    newCard.href = `/landmark?userId=${userId}&date=${date}`;
    newCard.className = "card";
    newCard.setAttribute("data-id", plannerCount); // Add this line to set data-id
        newCard.innerHTML = `
        <button class="delete-btn" title="Delete this planner">X</button>
        <a href="/landmark?plannerId=${plannerCount}&userId=${userId}">
             <h3>CATEGORY</h3>
           <h2>TOUR${plannerCount}</h2>
           
        </a>
    `;

    // NEW 카드 버튼 바로 앞에 새 카드 추가
    cardsContainer.insertBefore(newCard, newPlannerBtn);

    // 카드 삭제 버튼 이벤트
    const deleteBtn = newCard.querySelector(".delete-btn");
    deleteBtn.addEventListener("click", (event) => {
        event.preventDefault();
        event.stopPropagation();

        // Show confirmation alert
        const confirmDelete = confirm("planner를 정말 삭제하시겠습니까?");
        if (confirmDelete) {
            const plannerId = newCard.getAttribute("data-id");


            // 서버에 delete 요청
            fetch(`/api/planner/delete?plannerId=${plannerId}`,  {
                method: "DELETE"
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error("Failed to delete planner");
                    }
                    return response.text();
                })
                .then(message => {
                    alert(message);
                    // Remove the card from the DOM
                    cardsContainer.removeChild(newCard);
                })
                .catch(error => {
                    console.error("Error during deletion:", error);
                    alert("Planner 삭제 실패: " + error.message);
                });
        }
    });
});
// 관광지 리스트 번호 업데이트
function updateTourListNumbers() {
    console.log("Updating tour list numbers...");
    const cards = Array.from(cardsContainer.querySelectorAll('.card')); // 모든 카드 가져오기
    cards.forEach((card, index) => {
       // const cardId = card.getAttribute('data-id');
        const newTourNumber = index + 1; // 새로운 번호 계산
        card.querySelector('h2').textContent = `TOUR${newTourNumber}`;
        card.setAttribute('data-id', newTourNumber); // 데이터 속성도 업데이트
    });

    // 다음 생성될 플래너의 ID를 업데이트
    plannerCount = cards.length; // 마지막 카드 번호로 업데이트
    console.log("Tour list numbers updated, next planner count is:", plannerCount);
}


// WeatherApp API 호출
document.addEventListener("DOMContentLoaded", function () {
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
                const monthDay = `${date.getMonth() + 1}월 ${date.getDate()}일`;
                const iconCode = forecast.weather[0].icon;
                const iconUrl = `https://openweathermap.org/img/wn/${iconCode}.png`;
                const description = forecast.weather[0].description;
                const temp = `${Math.round(forecast.main.temp)}°C`; // 온도 반올림

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
        .catch(error => {
            console.error('Error during fetch request:', error);
            document.getElementById("weather-data").innerText = error.message;
        });
}); // Missing closing brace fixed
