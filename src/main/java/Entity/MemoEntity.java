package Entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Memo")
public class MemoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memo_id",nullable = false)
    private int memo_id;

    @Column(name = "planner_id",nullable = false)
    private int planner_id;

    @Column(name = "memo_content",nullable = true)
    private String memo_content;

    public MemoEntity() {
    }

    public MemoEntity(int memo_id, int planner_id, String memo_content) {
        this.memo_id = memo_id;
        this.planner_id = planner_id;
        this.memo_content = memo_content;
    }

    public int getMemo_id() {
        return memo_id;
    }

    public void setMemo_id(int memo_id) {
        this.memo_id = memo_id;
    }

    public int getPlanner_id() {
        return planner_id;
    }

    public void setPlanner_id(int planner_id) {
        this.planner_id = planner_id;
    }

    public String getMemo_content() {
        return memo_content;
    }

    public void setMemo_content(String memo_content) {
        this.memo_content = memo_content;
    }

}






