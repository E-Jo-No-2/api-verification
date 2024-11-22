package Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Theme")
@Data

public class ThemeEntity {

    @Id
    @Column(name = "theme_name", nullable = false, length = 50)
    private String themeName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

}
