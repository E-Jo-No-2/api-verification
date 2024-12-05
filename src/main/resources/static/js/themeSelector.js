var map;
var locations = [];
var markers = [];

// URL 파라미터에서 latitude와 longitude 값을 가져옴
const urlParams = new URLSearchParams(window.location.search);
const latitude = parseFloat(urlParams.get('latitude')) || 37.5665; // 기본값: 서울
const longitude = parseFloat(urlParams.get('longitude')) || 126.9780; // 기본값: 서울

// 지도 초기화 함수
function initializeMap(latitude, longitude) {
    if (map) {
        map.setCenter(new naver.maps.LatLng(latitude, longitude));
    } else {
        map = new naver.maps.Map("map", {
            center: new naver.maps.LatLng(latitude, longitude),
            zoom: 18 // 줌 레벨을 더 높게 설정
        });
    }
    // 좌표값을 콘솔에 출력
    console.log(`Map initialized with center at Latitude: ${latitude}, Longitude: ${longitude}`);
}

// 관광지 데이터를 서버에서 로드하는 함수
function loadTourSpots(longitude, latitude) {
    console.log(`Fetching nearby tour spots for Latitude: ${latitude}, Longitude: ${longitude}`);
    fetch(`/api/tour/nearby?longitude=${longitude}&latitude=${latitude}`)
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
}

// 모든 카드 요소 가져오기
const cards = document.querySelectorAll('.landmark-card');

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
                console.log(`Coordinates received for ${selectedLandmarkName} - Latitude: ${latitude}, Longitude: ${longitude}`);

                // 지도 초기화
                initializeMap(latitude, longitude);

                // 관광지 데이터 로드
                loadTourSpots(longitude, latitude);
            })
            .catch(error => console.error("Error fetching coordinates:", error));
    });
});

// 초기 지도 설정
initializeMap(latitude, longitude); // URL 파라미터에서 가져온 좌표값 사용
loadTourSpots(longitude, latitude); // URL 파라미터에서 가져온 좌표값 사용

// 데이터 형식 변환 함수
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
                        distance: location.Distance,
                        image: location.image,
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

// 지도에 마커 추가 함수
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

        // InfoWindow 생성
        const infoWindow = new naver.maps.InfoWindow({
            content: `
                <div style="padding:10px;min-width:250px;line-height:1.5;">
                    <h4 style="margin:0;">${location.location}</h4>
                    <img src="${location.image}" alt="Image" style="width:100%;height:auto;margin:10px 0;" />
                    <p><b>거리:</b> ${location.distance || '알 수 없음'}m</p>
                    <hr style="margin:10px 0;">
                    <button onclick="selectLocation('${location.location}', ${location.x}, ${location.y})" 
                            style="padding:5px 10px; background-color:#4CAF50; color:white; border:none; cursor:pointer;">장소 선택</button>
                    <button onclick="findRoute(${location.x}, ${location.y})" 
                            style="padding:5px 10px; background-color:#007BFF; color:white; border:none; cursor:pointer;">길찾기</button>
                </div>
            `,
            borderWidth: 1,
            disableAnchor: false
        });

        // 마커 클릭 이벤트
        naver.maps.Event.addListener(marker, 'click', () => {
            console.log("Marker clicked:", location.location);

            // InfoWindow가 열려 있다면 닫고, 아니면 열기
            if (infoWindow.getMap()) {
                infoWindow.close();
            } else {
                infoWindow.open(map, marker);
            }
        });

        markers.push(marker);
    });

    console.log("Markers added:", markers);
}

// 장소 선택 처리 함수
function selectLocation(name, lng, lat) {
    console.log("[INPUT] Marker clicked: Name =", name, "Longitude =", lng, "Latitude =", lat);
    addToTourList(name);

    // 서버로 데이터 전송
    const routeData = {
        start_point: `${lat},${lng}`, // 위도, 경도를 start_point로 저장
        end_point: null, // or some default value
        theme_name: name // 장소 이름을 theme_name으로 저장
    };
    console.log("[INPUT] Marker clicked: Name =", name, "Longitude =", lng, "Latitude =", lat);
    console.log("[OUTPUT] Sending data to server:", routeData);

    fetch('/api/route/save', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(routeData),
    })
        .then(response => {
            console.log("[RESPONSE] Received from server:", response);
            return response.json();
        })
        .then(data => console.log("[SERVER RESPONSE BODY]:", data))
        .catch(error => console.error("[ERROR] Server call failed:", error));
}

// 길찾기 처리 함수
function findRoute(lng, lat) {
    console.log("Finding route to:", lng, lat);
    alert(`길찾기를 위한 경도: ${lng}, 위도: ${lat}를 호출합니다.`);
}

// 관광지 리스트에 추가 함수
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

// 관광지 리스트 번호 업데이트 함수
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

// 테마 필터링 함수
function filterByTheme(theme) {
    console.log("Filtering locations by theme:", theme);
    const filteredLocations = locations.filter(location => !theme || location.theme === theme);
    console.log("Filtered locations:", filteredLocations);
    addMarkers(filteredLocations);
}

// 드롭다운 토글 함수
function toggleDropdown() {
    console.log("Toggling dropdown menu...");
    const dropdownMenu = document.getElementById("dropdownMenu");
    dropdownMenu.style.display = dropdownMenu.style.display === "block" ? "none" : "block";
    console.log("Dropdown menu state:", dropdownMenu.style.display);
}

// 테마 선택 함수
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

// 뒤로가기 버튼 클릭 이벤트 추가
const backButton = document.getElementById('backBtnBelow');
backButton.addEventListener('click', function() {
    if (confirm("처음으로 돌아가시겠습니까?")) {
        window.location.href = '/';
    }
});

// 완료 버튼
function completeTour() {
    console.log("Complete button clicked.");
    alert("플래너 작성 완료를 하시겠습니까?");
}



