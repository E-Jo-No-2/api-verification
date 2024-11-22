package Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "LandMark")
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

    // 기본 생성자 (required by JPA)
    public LandMarkEntity() { //생성자 overloading
    }

    // Constructor with parameters
    public LandMarkEntity(int landmark_id, String landmark_name, String theme_name, double longitude, double latitude) {
        this.landmark_id = landmark_id;
        this.landmark_name = landmark_name;
        this.theme_name = theme_name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    // Getters and Setters
    public int getLandmark_id() {
        return landmark_id;
    }

    public void setLandmark_id(int landmark_id) {
        this.landmark_id = landmark_id;
    }

    public String getLandmark_name() {
        return landmark_name;
    }

    public void setLandmark_name(String landmark_name) {
        this.landmark_name = landmark_name;
    }

    public String getTheme_name() {
        return theme_name;
    }

    public void setTheme_name(String theme_name) {
        this.theme_name = theme_name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
