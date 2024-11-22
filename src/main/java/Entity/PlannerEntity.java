package Entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "Planner")
@Data
@NoArgsConstructor
public class PlannerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "planner_id")
    private int plannerId;

    @Column(name = "user_id", length = 30, nullable = false)
    private String userId;

    @Column(name = "start_date", nullable = true)
    private LocalDate startDate;

    @Column(name = "theme_name", length = 50, nullable = false)
    private String themeName;


    public PlannerEntity(int plannerId, String userId, LocalDate startDate, String themeName) {
        this.plannerId = plannerId;
        this.userId = userId;
        this.startDate = startDate;
        this.themeName = themeName;
    }

}

