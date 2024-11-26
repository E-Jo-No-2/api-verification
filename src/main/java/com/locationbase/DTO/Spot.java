package com.locationbase.DTO;

public class Spot {

    private Integer spotId;
    private String spotName;
    private Double longitude;
    private Double latitude;

    // 생성자
    public Spot(Integer spotId, String spotName, Double longitude, Double latitude) {
        this.spotId = spotId;
        this.spotName = spotName;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    // Getters and Setters
    public Integer getSpotId() {
        return spotId;
    }

    public void setSpotId(Integer spotId) {
        this.spotId = spotId;
    }

    public String getSpotName() {
        return spotName;
    }

    public void setSpotName(String spotName) {
        this.spotName = spotName;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
