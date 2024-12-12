// 모든 카드 요소 가져오기
const cards = document.querySelectorAll('.landmark-card');
const nextButton = document.getElementById('next-btn');
const backButton = document.getElementById('back-btn');
let selectedLatitude = null;
let selectedLongitude = null;

// 현재 URL에서 plannerId와 userId를 추출하는 함수
function getUrlParams() {
    const urlParams = new URLSearchParams(window.location.search);
    return {
        plannerId: urlParams.get('plannerId'),
        userId: urlParams.get('userId')
    };
}

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
        fetch(`/api/landmark/coordinates?landmarkName=${encodeURIComponent(selectedLandmarkName)}`)
            .then(response => {
                console.log('Response status:', response.status); // 응답 상태 로그
                if (!response.ok) {
                    console.error('Failed to fetch coordinates. Status:', response.status);
                    throw new Error('Network response was not ok');
                }
                return response.json();  // JSON 파싱
            })
            .then(coordinates => {
                if (!coordinates.latitude || !coordinates.longitude) {
                    console.error('Invalid coordinates received:', coordinates);
                    throw new Error('Invalid coordinates received');
                }
                const { latitude, longitude } = coordinates;
                selectedLatitude = latitude;
                selectedLongitude = longitude;
                console.log(`Coordinates received for ${selectedLandmarkName} - Latitude: ${latitude}, Longitude: ${longitude}`);

                // 다음 버튼 활성화
                nextButton.disabled = false;
            })
            .catch(error => {
                console.error('Error fetching coordinates:', error);
                alert('좌표 정보를 가져오는 중 오류가 발생했습니다. 다시 시도해 주세요.');
            });
    });
});

// 다음 버튼 클릭 이벤트 추가
nextButton.addEventListener('click', () => {
    const { plannerId, userId } = getUrlParams(); // URL 파라미터 추출

    if (selectedLatitude && selectedLongitude) {
        console.log('Redirecting to theme selection with coordinates:', selectedLatitude, selectedLongitude);
        window.location.href = `/themaselect?latitude=${selectedLatitude}&longitude=${selectedLongitude}&plannerId=${plannerId}&userId=${userId}`; // URL 파라미터 유지
    } else {
        alert('먼저 랜드마크를 선택해주세요.');
    }
});

// 뒤로가기 버튼 클릭 이벤트 추가
backButton.addEventListener('click', () => {
    console.log('Redirecting to home page');
    window.location.href = '/';
});
