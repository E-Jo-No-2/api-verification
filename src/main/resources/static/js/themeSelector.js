// 지도 초기화
var map = new naver.maps.Map("map", {
    center: new naver.maps.LatLng(37.5796, 126.9769),
    zoom: 12
});

var locations = [];
var markers = [];

// 관광지 데이터를 서버에서 로드
console.log("Fetching nearby tour spots...");
fetch('/api/tour/nearby?longitude=126.9780&latitude=37.5665')
    .then(response => {
        console.log("Fetch response received:", response);
        return response.json();
    })
    .then(data => {
        console.log("Data received from server:", data);
        locations = transformLocations(data); // 데이터 변환
        console.log("Transformed locations:", locations);
        addMarkers(locations); // 지도에 마커 추가
    })
    .catch(error => console.error("Error loading JSON data:", error));

// 데이터 형식 변환
function transformLocations(data) {
    console.log("Transforming locations...");
    let transformed = [];
    Object.keys(data).forEach(theme => {
        const themeData = data[theme];
        if (Array.isArray(themeData)) {
            console.log(`Processing theme: ${theme}, number of locations: ${themeData.length}`);
            themeData.forEach(location => {
                if (location.longitude && location.latitude) {
                    transformed.push({
                        x: parseFloat(location.longitude), // 경도
                        y: parseFloat(location.latitude), // 위도
                        location: location.landmark_name || location.title,
                        theme: theme
                    });
                } else {
                    console.warn(`Skipping location with invalid coordinates: ${JSON.stringify(location)}`);
                }
            });
        } else {
            console.warn(`Expected array but got ${typeof themeData} for theme: ${theme}`);
        }
    });
    console.log("Transformed data:", transformed);
    return transformed;
}
// 지도에 마커 추가
function addMarkers(filteredLocations) {
    console.log("Adding markers to the map...");
    markers.forEach(marker => marker.setMap(null)); // 기존 마커 제거
    markers = []; // 마커 배열 초기화

    filteredLocations.forEach(location => {
        console.log(`Adding marker - Latitude: ${location.y}, Longitude: ${location.x}, Location: ${location.location}`);
        const marker = new naver.maps.Marker({
            position: new naver.maps.LatLng(location.y, location.x),
            map: map,
            title: location.location
        });

        // 마커 클릭 이벤트
        naver.maps.Event.addListener(marker, 'click', () => {
            console.log("Marker clicked:", location.location);
            addToTourList(location.location);
        });
        markers.push(marker);
    });
    console.log("Markers added:", markers);
}

// 관광지 리스트에 추가
function addToTourList(locationName) {
    console.log("Adding location to tour list:", locationName);
    const tourList = document.getElementById("tourList");

    // 중복 확인
    const existingItem = Array.from(tourList.children).find(
        item => item.querySelector(".tour-item").textContent.includes(locationName)
    );
    if (existingItem) {
        console.warn(`Location already added to tour list: ${locationName}`);
        return;
    }

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
        if (newName) {
            console.log("Location name updated from:", locationName, "to:", newName);
            textSpan.textContent = `${tourList.children.length}. ${newName}`;
        }
    };

    // 삭제 버튼
    const deleteBtn = document.createElement("button");
    deleteBtn.textContent = "삭제";
    deleteBtn.className = "delete-btn";
    deleteBtn.onclick = () => {
        console.log("Location removed from tour list:", locationName);
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
    console.log("Updating tour list numbers...");
    const tourList = document.getElementById("tourList");
    Array.from(tourList.children).forEach((item, index) => {
        const textSpan = item.querySelector(".tour-item");
        const itemText = textSpan.textContent.split(". ")[1];
        textSpan.textContent = `${index + 1}. ${itemText}`;
    });
    console.log("Tour list numbers updated.");
}

// 테마 필터링
function filterByTheme(theme) {
    console.log("Filtering locations by theme:", theme);
    const filteredLocations = locations.filter(location => !theme || location.theme === theme);
    console.log("Filtered locations:", filteredLocations);
    addMarkers(filteredLocations);
}

// 드롭다운 토글
function toggleDropdown() {
    console.log("Toggling dropdown menu...");
    const dropdownMenu = document.getElementById("dropdownMenu");
    dropdownMenu.style.display = dropdownMenu.style.display === "block" ? "none" : "block";
    console.log("Dropdown menu state:", dropdownMenu.style.display);
}

// 테마 선택
function selectTheme(value, label) {
    console.log("Theme selected:", label, "Value:", value);
    document.getElementById("selectedTheme").textContent = label;
    filterByTheme(value);
    document.getElementById("dropdownMenu").style.display = "none"; // 드롭다운 닫기
}

// 드롭다운 외부 클릭 시 닫기
window.addEventListener("click", event => {
    if (!event.target.closest(".dropdown-container")) {
        console.log("Closing dropdown menu due to external click.");
        document.getElementById("dropdownMenu").style.display = "none";
    }
});

// 뒤로가기
function goBack() {
    console.log("Go back button clicked.");
    alert("뒤로 가시겠습니까?");
}

// 완료 버튼
function completeTour() {
    console.log("Complete button clicked.");
    alert("플래너 작성 완료를 하시겠습니까?");
}
