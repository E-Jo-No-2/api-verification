package Entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Memo")
@Data
@NoArgsConstructor
public class MemoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memo_id",nullable = false)
    private int memo_id;

    @Column(name = "planner_id",nullable = false)
    private int planner_id;

    @Column(name = "memo_content",nullable = true)
    private String memo_content;


    public MemoEntity(int memo_id, int planner_id, String memo_content) {
        this.memo_id = memo_id;
        this.planner_id = planner_id;
        this.memo_content = memo_content;
    }


}






