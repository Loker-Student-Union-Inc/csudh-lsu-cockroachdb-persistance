package edu.csudh.lsu.persistence.repository.gamesroom.shift;

import edu.csudh.lsu.persistence.model.shift.ShiftReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShiftReportRepository extends JpaRepository<ShiftReport, String> {

}
