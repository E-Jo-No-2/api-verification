// 모든 카드 요소 가져오기
const cards = document.querySelectorAll('.landmark-card');
const nextButton = document.getElementById('next-btn');

// 클릭 이벤트 추가
cards.forEach(card => {
    card.addEventListener('click', () => {
        // 모든 카드에서 선택 상태 제거
        cards.forEach(c => c.classList.remove('selected'));

        // 클릭한 카드에 선택 상태 추가
        card.classList.add('selected');

        // 다음 버튼 활성화
        nextButton.disabled = false;

        // 선택된 랜드마크 데이터 확인 (디버깅용)
        console.log('Selected Landmark:', card.dataset.landmark);
    });
});
