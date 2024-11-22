package Domain.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "LandMark")
@Data
@NoArgsConstructor
public class LandMarkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "landmark_id",nullable = false)
    private int landmark_id;

    @Column(name = "landmark_name", length = 50, nullable = false)
    private String landmark_name;

    @Column(name = "theme_name", length = 30, nullable = false)
    private String theme_name;

    @Column(name = "longitude", precision = 9, scale = 6, nullable = false)
    private double longitude;

    @Column(name = "latitude", precision = 9, scale = 6, nullable = false)
    private double latitude;

    // Constructor with parameters
    public LandMarkEntity(int landmark_id, String landmark_name, String theme_name, double longitude, double latitude) {
        this.landmark_id = landmark_id;
        this.landmark_name = landmark_name;
        this.theme_name = theme_name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

}
