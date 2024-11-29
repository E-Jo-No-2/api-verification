// 지도 초기화
var map = new naver.maps.Map("map", {
    center: new naver.maps.LatLng(37.5796, 126.9769),
    zoom: 12
});

var locations = [];
var markers = [];

// 관광지 데이터를 서버에서 로드
fetch('/api/tour/nearby?longitude=126.9780&latitude=37.5665')
    .then(response => response.json())
    .then(data => {
        locations = transformLocations(data); // 데이터 변환
        addMarkers(locations); // 지도에 마커 추가
    })
    .catch(error => console.error("Error loading JSON data:", error));

// 데이터 형식 변환
function transformLocations(data) {
    let transformed = [];
    Object.keys(data).forEach(theme => {
        const themeData = data[theme];
        if (Array.isArray(themeData)) {
            themeData.forEach(location => {
                transformed.push({
                    x: parseFloat(location.longitude) || 126.9769, // 기본값: 경복궁 경도
                    y: parseFloat(location.latitude) || 37.5796, // 기본값: 경복궁 위도
                    location: location.landmark_name,
                    theme: theme
                });
            });
        } else {
            console.warn(`Expected array but got ${typeof themeData} for theme: ${theme}`);
        }
    });
    return transformed;
}

// 지도에 마커 추가
function addMarkers(filteredLocations) {
    markers.forEach(marker => marker.setMap(null)); // 기존 마커 제거
    markers = [];

    filteredLocations.forEach(location => {
        const marker = new naver.maps.Marker({
            position: new naver.maps.LatLng(location.x, location.y),
            map: map,
            title: location.location
        });

        // 마커 클릭 이벤트
        naver.maps.Event.addListener(marker, 'click', () => addToTourList(location.location));
        markers.push(marker);
    });
}

// 관광지 리스트에 추가
function addToTourList(locationName) {
    const tourList = document.getElementById("tourList");
    const listItem = document.createElement("li");
    const textSpan = document.createElement("span");
    textSpan.textContent = `${tourList.children.length + 1}. ${locationName}`;
    textSpan.className = "tour-item";

    // 수정 버튼
    const editBtn = document.createElement("button");
    editBtn.textContent = "수정";
    editBtn.className = "edit-btn";
    editBtn.onclick = () => {
        const newName = prompt("새로운 이름을 입력하세요:", locationName);
        if (newName) textSpan.textContent = `${tourList.children.length}. ${newName}`;
    };

    // 삭제 버튼
    const deleteBtn = document.createElement("button");
    deleteBtn.textContent = "삭제";
    deleteBtn.className = "delete-btn";
    deleteBtn.onclick = () => {
        tourList.removeChild(listItem);
        updateTourListNumbers();
    };

    listItem.appendChild(textSpan);
    listItem.appendChild(editBtn);
    listItem.appendChild(deleteBtn);
    tourList.appendChild(listItem);
}

// 관광지 리스트 번호 업데이트
function updateTourListNumbers() {
    const tourList = document.getElementById("tourList");
    Array.from(tourList.children).forEach((item, index) => {
        const textSpan = item.querySelector(".tour-item");
        const itemText = textSpan.textContent.split(". ")[1];
        textSpan.textContent = `${index + 1}. ${itemText}`;
    });
}

// 테마 필터링
function filterByTheme(theme) {
    const filteredLocations = locations.filter(location => !theme || location.theme === theme);
    addMarkers(filteredLocations);
}

// 드롭다운 토글
function toggleDropdown() {
    const dropdownMenu = document.getElementById("dropdownMenu");
    dropdownMenu.style.display = dropdownMenu.style.display === "block" ? "none" : "block";
}

// 테마 선택
function selectTheme(value, label) {
    document.getElementById("selectedTheme").textContent = label;
    filterByTheme(value);
    document.getElementById("dropdownMenu").style.display = "none"; // 드롭다운 닫기
}

// 드롭다운 외부 클릭 시 닫기
window.addEventListener("click", event => {
    if (!event.target.closest(".dropdown-container")) {
        document.getElementById("dropdownMenu").style.display = "none";
    }
});

// 뒤로가기
function goBack() {
    alert("뒤로 가시겠습니까?");
}

// 완료 버튼
function completeTour() {
    alert("플래너 작성 완료를 하시겠습니까?");
}
