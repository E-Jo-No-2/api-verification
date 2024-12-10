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
    console.log(`지도가 초기화되었습니다. 중심 좌표 - 위도: ${latitude}, 경도: ${longitude}`);
}

// 관광지 데이터를 서버에서 로드하는 함수
function loadTourSpots(longitude, latitude) {
    console.log(`위도: ${latitude}, 경도: ${longitude} 주변의 관광지 데이터를 불러오는 중입니다.`);
    fetch(`/api/tour/nearby?longitude=${longitude}&latitude=${latitude}`)
        .then(response => {
            console.log("서버 응답을 받았습니다:", response);
            return response.json();
        })
        .then(data => {
            console.log("서버로부터 받은 데이터:", data);
            locations = transformLocations(data); // 데이터 변환
            console.log("변환된 위치 데이터:", locations);
            addMarkers(locations); // 지도에 마커 추가
        })
        .catch(error => console.error("JSON 데이터를 불러오는 중 오류 발생:", error));
}

// 데이터 형식 변환 함수
function transformLocations(data) {
    console.log("위치 데이터를 변환 중입니다...");
    let transformed = [];
    Object.keys(data).forEach(theme => {
        const themeData = data[theme];
        if (Array.isArray(themeData)) {
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
                }
            });
        }
    });
    console.log("변환된 데이터:", transformed);
    return transformed;
}

// 지도에 마커 추가 함수
function addMarkers(filteredLocations) {
    console.log("지도에 마커를 추가하는 중입니다...");
    markers.forEach(marker => marker.setMap(null)); // 기존 마커 제거
    markers = []; // 마커 배열 초기화

    filteredLocations.forEach(location => {
        const marker = new naver.maps.Marker({
            position: new naver.maps.LatLng(location.y, location.x),
            map: map,
            title: location.location
        });

        // InfoWindow 생성
        const infoWindow = new naver.maps.InfoWindow({
            content: `
                <div style="padding:10px;min-width:250px;line-height:1.5;" id="info-window-${location.location}">
                    <h4 style="margin:0;">${location.location}</h4>
                    <img src="${location.image}" alt="Image" style="width:100%;height:auto;margin:10px 0;" />
                    <p><b>거리:</b> ${location.distance || '알 수 없음'}m</p>
                    <p id="rating-${location.location}"><b>평점:</b> 불러오는 중...</p>
                    <hr style="margin:10px 0;">
                    <button onclick="selectLocation('${location.location}', ${location.x}, ${location.y})" 
                            style="padding:5px 10px; background-color:#4CAF50; color:white; border:none; cursor:pointer;">장소 선택</button>
                    <button onclick="findRoute(${location.x}, ${location.y})" 
                            style="padding:5px 10px; background-color:#007BFF; color:white; border:none; cursor:pointer;">길찾기</button>
                </div>
            `
        });

        // 마커 클릭 이벤트
        naver.maps.Event.addListener(marker, 'click', () => {
            console.log("마커 클릭됨:", location.location);

            if (infoWindow.getMap()) {
                infoWindow.close();
            } else {
                infoWindow.open(map, marker);

                // **추가된 부분: 별점 데이터를 서버에서 가져오기**
                fetch(`/api/landmark/rating?name=${encodeURIComponent(location.location)}`)
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('별점 데이터를 불러오는 중 오류 발생');
                        }
                        return response.json();
                    })
                    .then(data => {
                        const averageRating = data.averageRating || 'N/A';
                        const reviewCount = data.reviewCount || 0;
                        const ratingElement = document.getElementById(`rating-${location.location}`);
                        ratingElement.innerHTML = `<b>평점:</b> ${averageRating} (${reviewCount}개 리뷰)`;
                    })
                    .catch(error => {
                        console.error("별점 데이터를 불러오는 중 오류 발생:", error);
                        const ratingElement = document.getElementById(`rating-${location.location}`);
                        ratingElement.innerHTML = `<b>평점:</b> 데이터를 불러오지 못했습니다.`;
                    });
            }
        });

        markers.push(marker);
    });

    console.log("마커 추가 완료:", markers);
}

// 장소 선택 처리 함수
function selectLocation(name, lng, lat) {
    console.log("[입력] 마커 클릭됨: 이름 =", name, "경도 =", lng, "위도 =", lat);
    addToTourList(name);

    const routeData = {
        lat: lat,
        lng: lng,
        name: name
    };

    fetch('/api/places/save', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(routeData),
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! 상태 코드: ${response.status}`);
            }
            return response.json();
        })
        .then(data => console.log("[서버 응답 본문]:", data))
        .catch(error => console.error("[오류] 서버 호출 실패:", error));
}

// 초기 지도 설정
initializeMap(latitude, longitude);
loadTourSpots(longitude, latitude);
