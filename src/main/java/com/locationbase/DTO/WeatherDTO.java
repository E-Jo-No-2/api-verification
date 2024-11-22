package DTO;

import java.util.Date;

public class WeatherDTO {

    private Date date;
    private String weather;
    private String iconCode;
    private String iconUrl;

    // 생성자
    public WeatherDTO(Date date, String weather, String iconCode, String iconUrl) {
        this.date = date;
        this.weather = weather;
        this.iconCode = iconCode;
        this.iconUrl = iconUrl;
    }

    // Getters and Setters
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getIconCode() {
        return iconCode;
    }

    public void setIconCode(String iconCode) {
        this.iconCode = iconCode;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
