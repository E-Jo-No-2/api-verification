package Domain.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Landmark")
public class LandMarkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int landmark_id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 50)
    private String theme_name;

    @Column(nullable = false)
    private double longitude;

    @Column(nullable = false)
    private double latitude;
}
