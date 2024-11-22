package com.locationbase.Controller;

import com.locationbase.Service.WeatherApp;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class WeatherController {

    @GetMapping("/")
    public String home() {
        return "redirect:/main.html";
    }

    @GetMapping("/main.html")
    public String main() {
        return "main"; // main.html 파일 이름에서 확장자를 제외한 이름 반환
    }

    @GetMapping("/getWeather")
    @ResponseBody
    public String getWeather() {
        String lang = "kr";
        // OpenWeatherMap API 키를 여기에 입력했습니다.
        String apiKey = "d33209554507a1997686d8feab67ab6a";
        JSONObject weatherData = WeatherApp.get5DayForecast(apiKey, lang);

        if (weatherData == null) {
            return "{\"error\": \"날씨 정보를 불러오는 도중 오류가 발생했습니다.\"}";
        }

        // 필요한 데이터만 추출
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

        return filteredData.toString();
    }
}
