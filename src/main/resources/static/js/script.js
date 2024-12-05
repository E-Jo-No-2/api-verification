// DOM 요소 가져오기
const cardsContainer = document.getElementById("cards");
const newPlannerBtn = document.getElementById("new-planner-btn");

// 초기화
let plannerCount = 0; // 플래너 ID 초기값
const userId = "testuser"; // 실제 사용자 ID로 대체

// 로컬 저장소에서 플래너 목록 불러오기
function loadPlannersFromLocalStorage() {
    const planners = JSON.parse(localStorage.getItem('planners')) || [];
    console.log("Loaded planners from localStorage:", planners); // 디버깅용

    if (planners.length > 0) {
        plannerCount = planners[planners.length - 1].id;  // 마지막 플래너의 ID를 plannerCount로 설정
    }

    planners.forEach(planner => {
        createPlannerCard(planner.id, planner.category);
    });
}

// NEW 버튼 클릭 이벤트
newPlannerBtn.addEventListener("click", () => {
    if (!cardsContainer) {
        alert("플래너를 추가할 수 없습니다. 페이지를 새로고침하세요.");
        return;
    }

    // 새로운 카드를 생성할 때는 실제 카드 수를 기반으로 번호를 매김
    plannerCount++; // 기존 카드 수에서 1 증가시켜 ID 생성

    const newCardData = {
        id: plannerCount,
        category: 'CATEGORY',  // 카테고리 데이터 추가
    };

    // 로컬 저장소에 플래너 정보 저장
    savePlannerToLocalStorage(newCardData);

    // 새 카드 추가
    createPlannerCard(newCardData.id, newCardData.category);

    // 새 카드를 추가 후 번호 재정렬
    updateTourListNumbers();
});

// 플래너 카드 생성
function createPlannerCard(id, category) {
    console.log(`Creating planner card with ID: ${id}, Category: ${category}`); // 디버깅용
    const newCard = document.createElement("div");
    newCard.className = "card";
    newCard.setAttribute("data-id", id);
    newCard.innerHTML = `
        <button class="delete-btn" title="플래너 삭제">X</button>
        <a href="/landmark?plannerId=${id}&userId=${userId}">
            <h3>${category}</h3>
            <h2>TOUR${id}</h2>
        </a>
    `;

    // NEW 카드 버튼 바로 앞에 새 카드 추가
    cardsContainer.insertBefore(newCard, newPlannerBtn);

    // 삭제 버튼 이벤트 리스너 추가
    const deleteBtn = newCard.querySelector(".delete-btn");
    deleteBtn.addEventListener("click", (event) => {
        event.preventDefault();
        event.stopPropagation();

        const confirmDelete = confirm("플래너를 정말 삭제하시겠습니까?");
        if (confirmDelete) {
            deletePlanner(id); // DELETE 요청 실행
        }
    });
}

// 로컬 저장소에 플래너 정보 저장
function savePlannerToLocalStorage(plannerData) {
    let planners = JSON.parse(localStorage.getItem('planners')) || [];
    planners.push(plannerData);
    localStorage.setItem('planners', JSON.stringify(planners));
}

// 플래너 삭제 요청
function deletePlanner(plannerId) {
    // 서버에 DELETE 요청 보내기
    fetch(`/api/planner/delete?plannerId=${plannerId}`, {
        method: "DELETE",
    })
        .then((response) => {
            if (!response.ok) {
                throw new Error("플래너 삭제 실패");
            }
            return response.text();
        })
        .then((message) => {
            alert(message); // 성공 메시지
            handleDeletePlannerSuccess(plannerId); // 성공 시 UI 업데이트
        })
        .catch((error) => {
            console.error("플래너 삭제 실패:", error);
            alert("플래너 삭제에 실패했습니다: " + error.message);
        });
}

// 플래너 삭제 후 UI 업데이트
function handleDeletePlannerSuccess(deletedPlannerId) {
    // UI에서 삭제
    const deletedCard = cardsContainer.querySelector(`[data-id="${deletedPlannerId}"]`);
    if (deletedCard) {
        cardsContainer.removeChild(deletedCard);
    }
    // 로컬 저장소에서 삭제
    removePlannerFromLocalStorage(deletedPlannerId);
    updateTourListNumbers(); // 순서 재정렬
}

// 로컬 저장소에서 플래너 삭제
function removePlannerFromLocalStorage(plannerId) {
    let planners = JSON.parse(localStorage.getItem('planners')) || [];
    planners = planners.filter(planner => planner.id !== plannerId);
    localStorage.setItem('planners', JSON.stringify(planners));
}

// 관광지 리스트 번호 업데이트
function updateTourListNumbers() {
    const cards = Array.from(cardsContainer.querySelectorAll(".card"));
    cards.forEach((card, index) => {
        const newTourNumber = index + 1;
        card.querySelector("h2").textContent = `TOUR${newTourNumber}`;
        card.setAttribute("data-id", newTourNumber);
    });

    // 마지막에 new 버튼을 다시 추가하여 위치를 보장
    cardsContainer.appendChild(newPlannerBtn);
    plannerCount = cards.length; // 현재 카드 수를 기반으로 plannerCount 갱신
    console.log("플래너 리스트 번호 업데이트 완료:", plannerCount);
}

// 페이지 로드 시 로컬 저장소에서 플래너 목록 불러오기
document.addEventListener("DOMContentLoaded", function () {
    loadPlannersFromLocalStorage();
});

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


// WeatherApp API 호출
    document.addEventListener("DOMContentLoaded", function () {
        fetch("/getWeather")
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Network response was not ok " + response.statusText);
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
