package com.locationbase.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "Theme")
@NoArgsConstructor
public class ThemeEntity {

    @Id
    @Column(length = 50,nullable = false)
    private String theme_name;

    @Column(columnDefinition = "TEXT")
    private String description;
}
