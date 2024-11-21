package Entity;


import jakarta.persistence.*;

@Entity
@Table(name = "Planner_Spot")
public class Planner_SpotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "planner_spot_id", nullable = false)
    private int planner_spot_id;

    @Column(name = " planner_id",nullable = false)
    private int planner_id;

    @Column(name = "spot_id", nullable = false)
    private int spot_id;

    @Column(name = "visit_order", nullable = true)
    private int visit_order;

    public Planner_SpotEntity() {}

    public Planner_SpotEntity(int planner_id, int spot_id, int visit_order) {
        this.planner_spot_id = planner_id;
        this.planner_id = planner_id;
        this.spot_id = spot_id;
        this.visit_order = visit_order;
    }

    public int getPlanner_spot_id() {
        return planner_spot_id;
    }

    public void setPlanner_spot_id(int planner_spot_id) {
        this.planner_spot_id = planner_spot_id;
    }

    public int getPlanner_id() {
        return planner_id;
    }

    public void setPlanner_id(int planner_id) {
        this.planner_id = planner_id;
    }

    public int getSpot_id() {
        return spot_id;
    }

    public void setSpot_id(int spot_id) {
        this.spot_id = spot_id;
    }

    public int getVisit_order() {
        return visit_order;
    }

    public void setVisit_order(int visit_order) {
        this.visit_order = visit_order;
    }

}
