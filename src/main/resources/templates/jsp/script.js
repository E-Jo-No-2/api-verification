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
    newCard.href = `planner${plannerCount}.html`; // 각 플래너의 세부 링크 설정
    newCard.className = "card";
    newCard.innerHTML = `
        <h3>CATEGORY</h3>
        <h2>TOUR${plannerCount}</h2>
        <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>
    `;

    // NEW 버튼 앞에 추가
    cardsContainer.insertBefore(newCard, newPlannerBtn);
});
