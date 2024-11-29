var map = new naver.maps.Map("map", {
    center: new naver.maps.LatLng(37.5665, 126.9780),
    zoom: 12
});

var locations = [];
var markers = [];

// JSON 데이터 로드
fetch('location_data.json')
    .then(response => response.json())
    .then(data => {
        locations = data;
        addMarkers(locations);
    })
    .catch(error => console.error("Error loading JSON data:", error));

function addMarkers(filteredLocations) {
    markers.forEach(marker => marker.setMap(null));
    markers = [];
    filteredLocations.forEach(function(location) {
        var marker = new naver.maps.Marker({
            position: new naver.maps.LatLng(location.x, location.y),
            map: map,
            title: location.location
        });
        naver.maps.Event.addListener(marker, 'click', function () {
            addToTourList(location.location);
        });
        markers.push(marker);
    });
}

function addToTourList(locationName) {
    var tourList = document.getElementById("tourList");
    var listItem = document.createElement("li");
    var textSpan = document.createElement("span");
    textSpan.textContent = `${tourList.children.length + 1}. ${locationName}`;
    textSpan.className = "tour-item";

    var editBtn = document.createElement("button");
    editBtn.textContent = "수정";
    editBtn.className = "edit-btn";
    editBtn.onclick = function () {
        var newName = prompt("새로운 이름을 입력하세요:", locationName);
        if (newName) {
            textSpan.textContent = `${tourList.children.length}. ${newName}`;
        }
    };

    var deleteBtn = document.createElement("button");
    deleteBtn.textContent = "삭제";
    deleteBtn.className = "delete-btn";
    deleteBtn.onclick = function () {
        tourList.removeChild(listItem);
        updateTourListNumbers();
    };

    listItem.appendChild(textSpan);
    listItem.appendChild(editBtn);
    listItem.appendChild(deleteBtn);
    tourList.appendChild(listItem);
}

function updateTourListNumbers() {
    var tourList = document.getElementById("tourList");
    var items = tourList.getElementsByTagName("li");
    for (var i = 0; i < items.length; i++) {
        var textSpan = items[i].getElementsByClassName("tour-item")[0];
        var itemText = textSpan.textContent.split(". ")[1];
        textSpan.textContent = `${i + 1}. ${itemText}`;
    }
}

function filterByTheme() {
    var selectedTheme = document.getElementById("themeSelect").value;
    var filteredLocations = locations.filter(function(location) {
        return selectedTheme === "" || location.theme === selectedTheme;
    });
    addMarkers(filteredLocations);
}

function goBack() {
    alert("뒤로 가시겠습니까?");
}

function completeTour() {
    alert("플래너 작성 완료를 하시겠습니까?");
}
function toggleDropdown() {
    const dropdownMenu = document.getElementById("dropdownMenu");
    dropdownMenu.style.display = dropdownMenu.style.display === "block" ? "none" : "block";
}

function selectTheme(value, label) {
    const selectedTheme = document.getElementById("selectedTheme");
    selectedTheme.textContent = label;

    // 기존 테마 필터링 함수 호출
    filterByTheme(value);

    // 드롭다운 닫기
    const dropdownMenu = document.getElementById("dropdownMenu");
    dropdownMenu.style.display = "none";
}

// 드롭다운 외부 클릭 시 닫기
window.addEventListener("click", function (event) {
    if (!event.target.closest(".dropdown-container")) {
        const dropdownMenu = document.getElementById("dropdownMenu");
        dropdownMenu.style.display = "none";
    }
});