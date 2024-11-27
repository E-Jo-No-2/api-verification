package com.locationbase.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "Weather")
@NoArgsConstructor
public class WeatherEntity {

    @Id
    @Column(name = "date")
    private LocalDate date; // Primary Key (date)

    @Column(name = "weather", length = 50)
    private String weather; // Weather description (nullable)

    @Column(name = "icon_code", length = 10)
    private String icon_code; // Weather icon code (nullable)

    @Column(name = "icon_url", length = 255)
    private String icon_url; // URL to the weather icon (nullable)

    // Constructor for WeatherEntity
    public WeatherEntity(LocalDate date, String weather, String icon_code, String icon_url) {
        this.date = date;
        this.weather = weather;
        this.icon_code = icon_code;
        this.icon_url = icon_url;
    }
}
