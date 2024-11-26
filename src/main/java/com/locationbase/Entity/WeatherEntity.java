package com.locationbase.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Weather")
@Data
@NoArgsConstructor
public class WeatherEntity {

    @Id
    @Column(name = "date")
    private LocalDate date;

    @Column(name = "temperature")
    private BigDecimal temperature;

    @Column(name = "condition")
    private String condition;

    public WeatherEntity(LocalDate date, BigDecimal temperature, String condition) {
        this.date = date;
        this.temperature = temperature;
        this.condition = condition;
    }

}
