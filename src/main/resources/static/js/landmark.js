// 모든 카드 요소 가져오기
const cards = document.querySelectorAll('.landmark-card');
const nextButton = document.getElementById('next-btn');
const backButton = document.getElementById('back-btn');
let selectedLatitude = null;
let selectedLongitude = null;

// 클릭 이벤트 추가
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
