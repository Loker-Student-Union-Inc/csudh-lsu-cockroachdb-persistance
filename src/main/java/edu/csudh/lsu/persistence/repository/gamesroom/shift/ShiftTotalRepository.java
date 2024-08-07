package edu.csudh.lsu.persistence.repository.gamesroom.shift;

import edu.csudh.lsu.persistence.model.shift.ShiftTotal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ShiftTotalRepository extends JpaRepository<ShiftTotal, UUID> {

}
