// DOM 요소 가져오기
const cardsContainer = document.getElementById("cards");
const newPlannerBtn = document.getElementById("new-planner-btn");
const nextButton = document.getElementById('next-btn');
const backButton = document.getElementById('back-btn');
let selectedLatitude = null;
let selectedLongitude = null;
let plannerCount = 0; // 플래너 ID 초기값
const userId = "testuser"; // 실제 사용자 ID로 대체

// 초기화
document.addEventListener("DOMContentLoaded", function () {
    if (cardsContainer && newPlannerBtn) {
        loadPlannerList(); // 로컬 저장소에서 플래너 목록 불러오기
        loadLandmarkCards(); // 랜드마크 카드 로딩
    } else {
        console.error("DOM 요소를 찾을 수 없습니다. HTML 구조를 확인하세요.");
    }
});

// 랜드마크 카드 클릭 시 좌표 선택 및 다음 버튼 활성화
const cards = document.querySelectorAll('.landmark-card');
cards.forEach(card => {
    card.addEventListener('click', () => {
        // 모든 카드에서 선택 상태 제거
        cards.forEach(c => c.classList.remove('selected'));

        // 클릭한 카드에 선택 상태 추가
        card.classList.add('selected');

        // 선택된 랜드마크 데이터 확인
        const selectedLandmarkName = card.dataset.landmark;
        console.log('Selected Landmark:', selectedLandmarkName);

        // 서버에서 좌표값 가져오기
        fetch(`/api/landmark/coordinates?landmarkName=${selectedLandmarkName}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(coordinates => {
                const { latitude, longitude } = coordinates;
                selectedLatitude = latitude;
                selectedLongitude = longitude;
                console.log(`Coordinates received for ${selectedLandmarkName} - Latitude: ${latitude}, Longitude: ${longitude}`);

                // 다음 버튼 활성화
                nextButton.disabled = false;
            })
            .catch(error => console.error("Error fetching coordinates:", error));
    });
});

// 다음 버튼 클릭 이벤트 추가
nextButton.addEventListener('click', () => {
    if (selectedLatitude && selectedLongitude) {
        window.location.href = `/themaselect?latitude=${selectedLatitude}&longitude=${selectedLongitude}`;
    } else {
        alert('먼저 랜드마크를 선택해주세요.');
    }
});

// 뒤로가기 버튼 클릭 이벤트 추가
backButton.addEventListener('click', () => {
    window.location.href = '/';
});

// NEW 버튼 클릭 이벤트
newPlannerBtn.addEventListener("click", () => {
    if (!cardsContainer) {
        alert("플래너를 추가할 수 없습니다. 페이지를 새로고침하세요.");
        return;
    }

    // 새로운 카드를 생성할 때는 실제 카드 수를 기반으로 번호를 매김
    const cards = Array.from(cardsContainer.querySelectorAll(".card"));
    plannerCount = cards.length + 1; // 기존 카드 수 + 1을 사용

    const date = new Date().toISOString().split("T")[0]; // 현재 날짜 가져오기
    const newCardData = {
        id: plannerCount,
        name: `TOUR${plannerCount}`,
        category: 'CATEGORY',  // 카테고리 데이터 추가
    };

    // 로컬 저장소에 플래너 데이터 저장
    savePlannerToLocalStorage(newCardData);

    const newCard = document.createElement("div");
    newCard.className = "card";
    newCard.setAttribute("data-id", plannerCount);
    newCard.innerHTML = `
        <button class="delete-btn" title="플래너 삭제">X</button>
        <a href="/landmark?plannerId=${plannerCount}&userId=${userId}">
            <h3>${newCardData.category}</h3>
            <h2>${newCardData.name}</h2>
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
            const plannerId = newCard.getAttribute("data-id");
            deletePlanner(plannerId); // DELETE 요청 실행
        }
    });

    // 새 카드를 추가 후 번호 재정렬
    updateTourListNumbers();
});

// 로컬 저장소에 플래너 정보 저장 함수
function savePlannerToLocalStorage(plannerData) {
    let planners = JSON.parse(localStorage.getItem('planners')) || [];
    planners.push(plannerData);
    localStorage.setItem('planners', JSON.stringify(planners));
}

// 로컬 저장소에서 플래너 목록 불러오기
function loadPlannerList() {
    const planners = JSON.parse(localStorage.getItem('planners')) || [];
    planners.forEach(planner => {
        const newCard = document.createElement("div");
        newCard.className = "card";
        newCard.setAttribute("data-id", planner.id);
        newCard.innerHTML = `
            <button class="delete-btn" title="플래너 삭제">X</button>
            <a href="/landmark?plannerId=${planner.id}&userId=${userId}">
                <h3>${planner.category}</h3>
                <h2>${planner.name}</h2>
            </a>
        `;
        cardsContainer.appendChild(newCard);

        const deleteBtn = newCard.querySelector(".delete-btn");
        deleteBtn.addEventListener("click", (event) => {
            event.preventDefault();
            event.stopPropagation();
            const confirmDelete = confirm("플래너를 정말 삭제하시겠습니까?");
            if (confirmDelete) {
                deletePlanner(planner.id);
            }
        });
    });
    updateTourListNumbers();
}

// 플래너 삭제 요청
function deletePlanner(plannerId) {
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

// DELETE 요청 성공 후 처리
function handleDeletePlannerSuccess(deletedPlannerId) {
    const deletedCard = cardsContainer.querySelector(`[data-id="${deletedPlannerId}"]`);
    if (deletedCard) {
        cardsContainer.removeChild(deletedCard);
    }
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

// 날씨 정보 불러오기
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
