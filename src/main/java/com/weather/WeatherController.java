package com.weather;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public String getWeather(@RequestParam String city) {
        String apiKey = "19635196e3440722d699cf6618188ca2";
        String lang = "kr";
        JSONObject weatherData = WeatherApp.getWeatherData(city, apiKey, lang);
        return weatherData.toString();
    }
}
