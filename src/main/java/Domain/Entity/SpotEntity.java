package Domain.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Spot")
public class SpotEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int spot_id;

    @Column(nullable = false,length = 50)
    private String spot_name;

    @Column(nullable = false,
    private double longitude;
    private double latitude;


}

