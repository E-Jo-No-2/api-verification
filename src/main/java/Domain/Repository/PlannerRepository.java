package Domain.Repository;

import Domain.Entity.PlannerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PlannerRepository extends JpaRepository<PlannerEntity, Integer> {

    // Find all planners by user ID
    List<PlannerEntity> findByUserId(String userId);

    // Find all planners by theme name
    List<PlannerEntity> findByThemeName(String themeName);

    // Find planners starting after a specific date
    List<PlannerEntity> findByStartDateAfter(LocalDate startDate);

    // Find planners within a date range
    List<PlannerEntity> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
}
