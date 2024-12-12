var map;
var locations = [];
var markers = [];

// URL 파라미터에서 latitude와 longitude 값을 가져옴
const urlParams = new URLSearchParams(window.location.search);
const latitude = parseFloat(urlParams.get('latitude')) || 37.5665; // 기본값: 서울
const longitude = parseFloat(urlParams.get('longitude')) || 126.9780; // 기본값: 서울
const plannerId = urlParams.get('plannerId');
const userId = urlParams.get('userId');

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
        console.log('선택된 랜드마크:', selectedLandmarkName);

        // 서버에서 좌표값 가져오기
        fetch(`/api/landmark/coordinates?landmarkName=${selectedLandmarkName}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('네트워크 응답이 정상적이지 않습니다');
                }
                return response.json();
            })
            .then(coordinates => {
                const { latitude, longitude } = coordinates;
                console.log(`좌표를 받았습니다: ${selectedLandmarkName} - 위도: ${latitude}, 경도: ${longitude}`);

                // 지도 초기화
                initializeMap(latitude, longitude);

                // 관광지 데이터 로드
                loadTourSpots(longitude, latitude);
            })
            .catch(error => console.error("좌표 데이터를 불러오는 중 오류 발생:", error));
    });
});

// 초기 지도 설정
initializeMap(latitude, longitude); // URL 파라미터에서 가져온 좌표값 사용
loadTourSpots(longitude, latitude); // URL 파라미터에서 가져온 좌표값 사용

// 데이터 형식 변환 함수
function transformLocations(data) {
    console.log("위치 데이터를 변환 중입니다...");
    let transformed = [];
    Object.keys(data).forEach(theme => {
        const themeData = data[theme];
        if (Array.isArray(themeData)) {
            console.log(`테마 처리 중: ${theme}, 위치 수: ${themeData.length}`);
            themeData.forEach(location => {
                if (location.longitude && location.latitude && location.landmarkName) { // landmarkName 검증 추가
                    transformed.push({
                        x: parseFloat(location.longitude), // 경도
                        y: parseFloat(location.latitude), // 위도
                        location: location.landmarkName || location.title,
                        distance: location.distance,
                        image: location.image || '', // image 검증 추가
                        theme: theme
                    });
                } else {
                    console.warn(`유효하지 않은 좌표 또는 이름을 가진 위치 건너뜀: ${JSON.stringify(location)}`);
                }
            });
        } else {
            console.warn(`배열이 예상되었지만 ${typeof themeData}을(를) 받았습니다: ${theme}`);
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
        if (location.location && location.location !== "") { // location 검증 추가
            console.log(`마커 추가 중 - 위도: ${location.y}, 경도: ${location.x}, 위치: ${location.location}`);
            const marker = new naver.maps.Marker({
                position: new naver.maps.LatLng(location.y, location.x),
                map: map,
                title: location.location
            });

            // InfoWindow 초기 내용
            const infoWindow = new naver.maps.InfoWindow({
                content: `
                    <div style="padding:10px;min-width:250px;line-height:1.5;">
                        <h4 style="margin:0;">${location.location}</h4>
                        <img src="${location.image}" alt="Image" style="width:100%;height:auto;margin:10px 0;" />
                        <p><b>거리:</b> ${location.distance || '알 수 없음'}m</p>
                        <div id="rating-${location.location}" style="margin-top:10px;">
                            <p>평점 데이터를 불러오는 중...</p>
                        </div>
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
                console.log("마커 클릭됨:", location.location);

                // InfoWindow가 열려 있다면 닫고, 아니면 열기
                if (infoWindow.getMap()) {
                    infoWindow.close();
                } else {
                    infoWindow.open(map, marker);

                    // 평점 데이터를 서버에서 가져와 업데이트
                    fetch(`/api/landmark/rating?name=${encodeURIComponent(location.location)}`)
                        .then(response => {
                            if (!response.ok) {
                                throw new Error(`HTTP 오류: ${response.status}`);
                            }
                            return response.json();
                        })
                        .then(data => {
                            console.log(`[성공] ${location.location}의 평점 데이터:`, data);

                            const averageRating = data.averageRating || 0.0;
                            const reviewCount = data.reviewCount || 0;

                            // 평점 데이터를 표시할 HTML
                            const ratingHtml = `
                                <p><b>평균 평점:</b> ${averageRating.toFixed(1)} / 5</p>
                                <p><b>리뷰 개수:</b> ${reviewCount}개</p>
                            `;

                            // InfoWindow 내부의 평점 데이터 영역 업데이트
                            document.getElementById(`rating-${location.location}`).innerHTML = ratingHtml;
                        })
                        .catch(error => {
                            console.error(`[오류] ${location.location}의 평점 데이터를 가져오는 중 문제 발생:`, error.message);
                            const errorHtml = `<p style="color:red;">평점 데이터를 불러오지 못했습니다.</p>`;
                            document.getElementById(`rating-${location.location}`).innerHTML = errorHtml;
                        });
                }
            });

            markers.push(marker);
        } else {
            console.warn(`유효하지 않은 위치를 가진 마커 건너뜀: ${location}`);
        }
    });

    console.log("마커 추가 완료:", markers);
}


// 장소 선택 처리 함수
function selectLocation(name, lng, lat) {
    console.log("[입력] 마커 클릭됨: 이름 =", name, "경도 =", lng, "위도 =", lat);
    addToTourList(name);

    // 서버로 데이터 전송
    const routeData = {
        lat: lat, // 위도
        lng: lng, // 경도
        name: name // 장소 이름
    };

    console.log("[출력] 서버로 데이터 전송 중:", routeData);

    fetch('/api/places/save', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(routeData),
    })
        .then(response => {
            console.log("[응답] 서버로부터 응답 받음:", response);
            console.log("[출력] 서버 응답 상태 코드:", response.status);

            if (response.status === 409) { // HTTP 409: Conflict (중복 데이터)
                return response.json().then(data => {
                    throw new Error(data.message); // 이후 then 블록이 실행되지 않도록 예외 발생
                });
            }

            if (!response.ok) { // 기타 오류 처리
                throw new Error(`HTTP 오류! 상태 코드: ${response.status}`);
            }

            return response.json(); // 성공 시 JSON 데이터를 반환
        })
        .then(data => {
            console.log("[서버 응답 본문]:", data);
            alert("장소가 성공적으로 저장되었습니다!");
        })
        .catch(error => {
            console.error("[오류] 서버 호출 실패:", error);
            // 중복 데이터 메시지는 이미 표시되었으므로 기타 오류만 처리
            if (error.message !== "장소가 이미 존재합니다!") {
                // alert("서버 호출 중 오류가 발생했습니다.");
            }
        });
}

// 길찾기 처리 함수
function findRoute(lng, lat) {
    console.log("길찾기 중:", lng, lat);
    alert(`길찾기를 위한 경도: ${lng}, 위도: ${lat}를 호출합니다.`);
}

// 관광지 리스트에 추가 함수
function addToTourList(locationName) {
    console.log("관광지 리스트에 추가 중:", locationName);
    const tourList = document.getElementById("tourList");

    const existingItem = Array.from(tourList.children).find(
        item => item.querySelector(".tour-item").textContent.includes(locationName)
    );
    if (existingItem) {
        console.warn(`관광지가 이미 리스트에 추가되어 있습니다: ${locationName}`);
        return;
    }

    const listItem = document.createElement("li");
    const textSpan = document.createElement("span");
    textSpan.textContent = `${tourList.children.length + 1}. ${locationName}`;
    textSpan.className = "tour-item";

    const editBtn = document.createElement("button");
    editBtn.textContent = "수정";
    editBtn.className = "edit-btn";
    editBtn.onclick = () => {
        const newName = prompt("새로운 이름을 입력하세요:", locationName);
        if (newName) {
            console.log("관광지 이름 변경:", locationName, "->", newName);
            textSpan.textContent = `${tourList.children.length}. ${newName}`;
        }
    };

    const deleteBtn = document.createElement("button");
    deleteBtn.textContent = "삭제";
    deleteBtn.className = "delete-btn";
    deleteBtn.onclick = () => {
        const placeId = parseInt(prompt("삭제할 장소의 ID를 입력하세요:"));
        if (placeId) {
            deletePlace(placeId, listItem);
        }
    };

    listItem.appendChild(textSpan);
    listItem.appendChild(editBtn);
    listItem.appendChild(deleteBtn);
    tourList.appendChild(listItem);
}

// 관광지 리스트 번호 업데이트 함수
function updateTourListNumbers() {
    console.log("관광지 리스트 번호 업데이트 중...");
    const tourList = document.getElementById("tourList");
    Array.from(tourList.children).forEach((item, index) => {
        const textSpan = item.querySelector(".tour-item");
        const itemText = textSpan.textContent.split(". ")[1];
        textSpan.textContent = `${index + 1}. ${itemText}`;
    });
    console.log("관광지 리스트 번호가 업데이트되었습니다.");
}

// 테마 필터링 함수
function filterByTheme(theme) {
    console.log("테마별로 위치 필터링 중:", theme);
    const filteredLocations = locations.filter(location => !theme || location.theme === theme);
    console.log("필터링된 위치:", filteredLocations);
    addMarkers(filteredLocations);
}

// 삭제 버튼 클릭 이벤트 추가 함수
function deletePlace(id, listItem) {
    console.log("[입력] 삭제 버튼 클릭됨: ID =", id);

    fetch(`/api/places/delete/${id}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP 오류! 상태 코드: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log("[서버 응답 본문]:", data);
            alert(data.message);
            listItem.remove(); // 리스트 항목 삭제
            updateTourListNumbers(); // 리스트 번호 업데이트
        })
        .catch(error => {
            console.error("[오류] 서버 호출 실패:", error);
            alert("장소 삭제 중 오류가 발생했습니다.");
        });
}

// 드롭다운 토글 함수
function toggleDropdown() {
    console.log("드롭다운 메뉴를 토글 중...");
    const dropdownMenu = document.getElementById("dropdownMenu");
    dropdownMenu.style.display = dropdownMenu.style.display === "block" ? "none" : "block";
    console.log("드롭다운 메뉴 상태:", dropdownMenu.style.display);
}

// 테마 선택 함수
function selectTheme(value, label) {
    console.log("테마 선택됨:", label, "값:", value);
    document.getElementById("selectedTheme").textContent = label;
    filterByTheme(value);
    document.getElementById("dropdownMenu").style.display = "none"; // 드롭다운 닫기
}

// 드롭다운 외부 클릭 시 닫기
window.addEventListener("click", event => {
    if (!event.target.closest(".dropdown-container")) {
        console.log("외부 클릭으로 인해 드롭다운 메뉴 닫기.");
        document.getElementById("dropdownMenu").style.display = "none";
    }
}, { passive: true });

// 뒤로가기 버튼 클릭 이벤트 추가
const backButton = document.getElementById('backBtnBelow');
backButton.addEventListener('click', function() {
    if (confirm("처음으로 돌아가시겠습니까?")) {
        window.location.href = '/';
    }
});

// 완료 버튼
function completeTour() {
    console.log("완료 버튼 클릭됨.");
    alert("플래너 작성 완료를 하시겠습니까?");
}

// URL에서 plannerId 가져오기
if (!plannerId) {
    alert("plannerId가 설정되지 않았습니다!");
    throw new Error("plannerId가 필요합니다.");
}

// 메모 불러오기 함수
function loadMemo(plannerId) {
    const uri = `/memos/planner/${plannerId}`; // URI 생성
    console.log("메모 불러오기 요청: plannerId =", plannerId);
    console.log("요청 URI:", uri);
    fetch(uri, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`메모 불러오기 실패: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log("메모 불러오기 성공:", data);
            memoId = data[0].memoId;
            console.log("메모 ID", memoId);
            // 메모를 화면의 textarea에 표시
            document.getElementById("memo").value = data[0]?.memoContent || ''; // 첫 번째 메모 표시
        })
        .catch(error => {
            console.error("메모 불러오기 실패:", error.message);
            alert("메모를 불러오지 못했습니다.");
        });
}

// 메모 업데이트 함수
function updateMemo(memoId, memoContent) {
    console.log("메모 수정 요청: memoId =", memoId, "memoContent =", memoContent);

    if (!memoId) {
        alert("메모 ID가 설정되지 않았습니다!");
        return;
    }

    if (!memoContent) {
        alert("메모 내용을 입력하세요!");
        return;
    }

    const memoData = {
        memoContent: memoContent
    };

    fetch(`/memos/${memoId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(memoData)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`메모 수정 실패: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log("메모 수정 성공:", data);
            alert("메모가 성공적으로 수정되었습니다!");
        })
        .catch(error => {
            console.error("메모 수정 실패:", error.message);
            alert("메모 수정에 실패했습니다.");
        });
}

// 초기 로드 및 이벤트 핸들러 설정
document.addEventListener("DOMContentLoaded", () => {
    // 메모 불러오기
    loadMemo(plannerId);

    // 저장 버튼 클릭 시 메모 저장
    const saveMemoBtn = document.getElementById("saveMemoBtn");
    if (saveMemoBtn) {
        saveMemoBtn.addEventListener("click", () => {
            const memoContent = document.getElementById("memo").value;
            updateMemo(memoId, memoContent);
        });
    } else {
        console.error("저장 버튼(saveMemoBtn)을 찾을 수 없습니다!");
    }

    // '플래너 완료' 버튼 클릭 시 '/map'으로 이동
    const completeTourBtn = document.getElementById("completeTourBtn");
    if (completeTourBtn) {
        completeTourBtn.addEventListener("click", () => {
            if (confirm("플래너 작성 완료를 하시겠습니까?")) {
                window.location.href = `/map?latitude=${latitude}&longitude=${longitude}&plannerId=${plannerId}&userId=${userId}`;
            }
        });
    } else {
        console.error("완료 버튼(completeTourBtn)을 찾을 수 없습니다!");
    }
});
