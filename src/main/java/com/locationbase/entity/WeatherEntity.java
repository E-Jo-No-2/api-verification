package com.locationbase.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "weather")
@NoArgsConstructor
public class WeatherEntity {

    @Id
    @Column(name = "date")
    private LocalDate date; // Primary Key (date)

    @Column(name = "weather", length = 50)
    private String weather; // Weather main field (nullable)

    @Column(name = "icon_code", length = 10)
    private String iconCode; // Weather icon code (nullable)

    @Column(name = "icon_url", length = 255)
    private String iconUrl; // URL to the weather icon (nullable)

    // Constructor for WeatherEntity
    public WeatherEntity(LocalDate date, String weather, String iconCode, String iconUrl) {
        this.date = date;
        this.weather = weather;
        this.iconCode = iconCode;
        this.iconUrl = iconUrl;
    }
}
