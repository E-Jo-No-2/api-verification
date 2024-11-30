package com.locationbase.Controller;

import com.locationbase.Service.WeatherService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Controller
public class WeatherController {

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);

    private final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/main";
    }

    @GetMapping("/main")
    public String main() {
        logger.debug("Navigating to main page and saving weather data.");
        updateWeatherData(); // main 페이지로 이동할 때 날씨 정보를 저장
        return "main"; // main.html 파일 이름에서 확장자를 제외한 이름 반환
    }

    @GetMapping("/getWeather")
    @ResponseBody
    public String getWeather() {
        String lang = "kr";
        // OpenWeatherMap API 키를 여기에 입력했습니다.
        String apiKey = "d33209554507a1997686d8feab67ab6a";
        JSONObject weatherData = WeatherService.get5DayForecast(apiKey, lang);

        if (weatherData == null) {
            return "{\"error\": \"날씨 정보를 불러오는 도중 오류가 발생했습니다.\"}";
        }

        // 날씨 데이터를 데이터베이스에 저장
        weatherService.saveWeatherData(weatherData);

        // 필요한 데이터만 추출
        JSONObject filteredData = filterWeatherData(weatherData);
        logger.debug("Filtered weather data: {}", filteredData);

        return filteredData.toString();
    }

    private void updateWeatherData() {
        String lang = "kr";
        // OpenWeatherMap API 키를 여기에 입력했습니다.
        String apiKey = "d33209554507a1997686d8feab67ab6a";
        JSONObject weatherData = WeatherService.get5DayForecast(apiKey, lang);

        logger.debug("Fetched weather data: {}", weatherData);

        if (weatherData != null) {
            logger.debug("Saving weather data from main page.");
            // 필요한 데이터만 추출하고 저장
            weatherService.saveWeatherData(weatherData);
        } else {
            logger.debug("No weather data to save.");
        }
    }

    private JSONObject filterWeatherData(JSONObject weatherData) {
        JSONArray weatherList = weatherData.getJSONArray("list");
        List<JSONObject> filteredList = new ArrayList<>();

        for (int i = 0; i < weatherList.length(); i++) {
            JSONObject item = weatherList.getJSONObject(i);
            String dateTime = item.getString("dt_txt");
            if (dateTime.endsWith("00:00:00")) {
                filteredList.add(item);
            }
        }

        JSONObject filteredData = new JSONObject();
        filteredData.put("list", filteredList);
        return filteredData;
    }
}
