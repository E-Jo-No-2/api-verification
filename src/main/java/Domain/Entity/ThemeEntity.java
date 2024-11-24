package Domain.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Theme")
public class ThemeEntity {

    @Id
    @Column(length = 50,nullable = false)
    private String themeName;

    @Column(columnDefinition = "TEXT")
    private String description;
}
