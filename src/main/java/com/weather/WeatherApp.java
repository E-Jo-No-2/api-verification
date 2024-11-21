package com.weather;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static java.nio.charset.StandardCharsets.UTF_8;

public class WeatherApp {
    public static JSONObject getWeatherData(String city, String apiKey, String lang) {
        JSONObject data = null;
        try {
            city = URLEncoder.encode(city, UTF_8);
            String apiURL = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&lang=" + lang + "&units=metric";
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
                System.out.println("GET 요청에 실패. 응답 코드: " + responseCode);
            }
        } catch (Exception e) {
            System.out.println("날씨 정보를 가져오는 도중 오류가 발생했습니다.");
            e.printStackTrace();
        }
        return data;
    }
}
