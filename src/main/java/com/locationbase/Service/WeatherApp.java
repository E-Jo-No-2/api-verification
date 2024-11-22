package com.locationbase.Service;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherApp {
    private static final Logger logger = LoggerFactory.getLogger(WeatherApp.class);

    public static JSONObject get5DayForecast(String apiKey, String lang) {
        JSONObject data = null;
        try {
            double lat = 37.5665; // 서울시의 위도
            double lon = 126.9780; // 서울시의 경도
            String apiURL = "https://api.openweathermap.org/data/2.5/forecast?lat=" + lat + "&lon=" + lon + "&appid=" + apiKey + "&lang=" + lang + "&units=metric";
            URL url = new URL(apiURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                data = new JSONObject(response.toString());
            } else {
                logger.error("GET 요청에 실패. 응답 코드: " + responseCode);
            }
        } catch (Exception e) {
            logger.error("날씨 정보를 가져오는 도중 오류가 발생했습니다.", e);
        }
        return data;
    }
}
