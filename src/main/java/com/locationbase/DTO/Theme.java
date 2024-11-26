package com.locationbase.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Theme")
public class Theme {

    @Id
    @Column(name = "theme_name", nullable = false, length = 50)
    private String themeName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    // Getter and Setter
    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
