package com.locationbase.Service;

import com.locationbase.Domain.Repository.WeatherRepository;
import com.locationbase.Entity.WeatherEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {
    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    private final WeatherRepository weatherRepository;

    @Autowired
    public WeatherService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

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
                logger.debug("Weather data fetched: {}", data);
            } else {
                logger.error("GET 요청에 실패. 응답 코드: " + responseCode);
            }
        } catch (Exception e) {
            logger.error("날씨 정보를 가져오는 도중 오류가 발생했습니다.", e);
        }
        return data;
    }

    @Transactional
    public void saveWeatherData(JSONObject weatherData) {
        logger.debug("saveWeatherData called with data: {}", weatherData);

        if (weatherData == null) {
            logger.error("날씨 데이터를 저장할 수 없습니다. 데이터가 null입니다.");
            return;
        }

        JSONArray weatherList = weatherData.getJSONArray("list");
        List<WeatherEntity> weatherEntities = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (int i = 0; i < weatherList.length(); i++) {
            JSONObject item = weatherList.getJSONObject(i);
            String dateTime = item.getString("dt_txt");
            if (dateTime.endsWith("00:00:00")) {
                LocalDate date = LocalDate.parse(dateTime.substring(0, 10), formatter);
                String weather = item.getJSONArray("weather").getJSONObject(0).getString("main");
                String iconCode = item.getJSONArray("weather").getJSONObject(0).getString("icon");
                String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + ".png";

                WeatherEntity weatherEntity = new WeatherEntity(date, weather, iconCode, iconUrl);
                weatherEntities.add(weatherEntity);
                logger.debug("Added weather entity: {}", weatherEntity);
            }
        }

        if (!weatherEntities.isEmpty()) {
            logger.debug("Saving weather data: {}", weatherEntities);
            weatherRepository.saveAll(weatherEntities);
        } else {
            logger.debug("No weather data to save.");
        }
    }
}
