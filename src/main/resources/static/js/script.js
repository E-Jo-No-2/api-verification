// Weather API 호출하기
document.addEventListener("DOMContentLoaded", function () {
    fetch("/getWeather")
        .then((response) => {
            if (!response.ok) {
                throw new Error("날씨 데이터를 불러오는 데 실패했습니다: " + response.statusText);
            }
            return response.json();
        })
        .then((data) => {
            console.log("날씨 데이터:", data);
            if (data.error) {
                throw new Error(data.error);
            }

            const forecasts = data.list || [];
            let weatherHtml = "";
            forecasts.forEach((forecast) => {
                const date = new Date(forecast.dt * 1000);
                const monthDay = `${date.getMonth() + 1}월 ${date.getDate()}일`; // 템플릿 리터럴로 수정
                const iconCode = forecast.weather[0].icon;
                const iconUrl = `https://openweathermap.org/img/wn/${iconCode}.png`; // 템플릿 리터럴로 수정
                const description = forecast.weather[0].description;
                const temp = `${Math.round(forecast.main.temp)}°C`; // 템플릿 리터럴로 수정

                weatherHtml += `
                    <div class="weather-forecast">
                        <div class="weather-icon">
                            <img src="${iconUrl}" alt="Weather Icon">
                        </div>
                        <h3>${monthDay}</h3>
                        <p>${description}</p>
                        <p>${temp}</p>
                    </div>
                `;
            });

            document.getElementById("weather-data").innerHTML = weatherHtml;
        })
        .catch((error) => {
            console.error("날씨 데이터를 불러오지 못했습니다:", error);
            document.getElementById("weather-data").innerText = "날씨 데이터를 가져오지 못했습니다.";
        });
});

// 플래너 추가 버튼 클릭 이벤트
const newPlannerBtn = document.getElementById("new-planner-btn");

newPlannerBtn.addEventListener("click", () => {
    console.log("new 버튼 클릭")
    // POST 요청을 보내서 planner를 저장
    fetch("/api/planner/save", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded" // 헤더 추가
        },
        body: new URLSearchParams({
            userId: "testuser"  // 실제 사용자 ID로 교체 필요
        })
    })
        .then(response => {
            console.log("서버 응답:", response);  // 서버 응답 로그 출력
            return response.json();  // JSON 파싱
        })
        .then(data => {
            console.log("응답 데이터:", data);  // 파싱된 데이터 출력
            if (data.plannerId) {
                // plannerId가 생성되면 SelectLandmark 페이지로 이동
                window.location.href = `/landmark?plannerId=${data.plannerId}&userId=testuser`; // 템플릿 리터럴로 수정
            } else {
                console.log("페이지 이동 실패");
            }
        })
        .catch(error => {
            console.error("플래너 저장 중 오류 발생:", error);  // 에러 로그 출력
            alert("플래너 저장 중 오류가 발생했습니다.");
        });
});
/*
// 플래너 생성 요청
function savePlanner() {
    const userId = document.getElementById("userId").value;

    // 사용자 ID가 비어있지 않은지 확인
    if (!userId) {
        alert("사용자 ID를 입력해주세요.");
        return;
    }

    fetch('/api/planner/save', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ userId: userId }) // userId만 서버로 전달
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("플래너 저장에 실패했습니다.");
            }
            return response.json();
        })
        .then(data => {
            console.log("플래너 생성 성공. 생성된 plannerId:", data.plannerId);

            if (data.plannerId) {
                const plannerId = data.plannerId;

                // 플래너 생성 후 날씨 데이터를 가져오는 함수 호출
                fetchWeatherData(plannerId);

                // 플래너 생성 후 landmark 페이지로 이동
                window.location.href = `/landmark?plannerId=${plannerId}&userId=${userId}`;  // 템플릿 리터럴로 수정
            } else {
                console.log("플래너 생성 실패");
            }
        })
        .catch(error => {
            console.error("플래너 생성 중 오류:", error);
            alert("플래너 생성 중 오류가 발생했습니다.");
        });
}
// Weather API 호출하기
document.addEventListener("DOMContentLoaded", function () {
    fetch("/getWeather")
        .then((response) => {
            if (!response.ok) {
                throw new Error("날씨 데이터를 불러오는 데 실패했습니다: " + response.statusText);
            }
            return response.json();
        })
        .then((data) => {
            console.log("날씨 데이터:", data);
            if (data.error) {
                throw new Error(data.error);
            }

            const forecasts = data.list || [];
            let weatherHtml = "";
            forecasts.forEach((forecast) => {
                const date = new Date(forecast.dt * 1000);
                const monthDay = `${date.getMonth() + 1}월 ${date.getDate()}일`; // 템플릿 리터럴로 수정
                const iconCode = forecast.weather[0].icon;
                const iconUrl = `https://openweathermap.org/img/wn/${iconCode}.png`; // 템플릿 리터럴로 수정
                const description = forecast.weather[0].description;
                const temp = `${Math.round(forecast.main.temp)}°C`; // 템플릿 리터럴로 수정

                weatherHtml += `
                    <div class="weather-forecast">
                        <div class="weather-icon">
                            <img src="${iconUrl}" alt="Weather Icon">
                        </div>
                        <h3>${monthDay}</h3>
                        <p>${description}</p>
                        <p>${temp}</p>
                    </div>
                `;
            });

            document.getElementById("weather-data").innerHTML = weatherHtml;
        })
        .catch((error) => {
            console.error("날씨 데이터를 불러오지 못했습니다:", error);
            document.getElementById("weather-data").innerText = "날씨 데이터를 가져오지 못했습니다.";
        });
});
*/
