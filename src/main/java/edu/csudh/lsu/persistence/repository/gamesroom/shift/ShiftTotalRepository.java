package edu.csudh.lsu.persistence.repository.gamesroom.shift;

import edu.csudh.lsu.persistence.model.shift.ShiftTotal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.UUID;

@Repository
public interface ShiftTotalRepository extends JpaRepository<ShiftTotal, UUID> {

    // Upsert a shift total
    @Modifying
    @Transactional
    @Query(value = "UPSERT INTO SHIFT_TOTAL (ID, STUDENT_NAME, ATTENDANT_NAME, ACTIVITY, COST, PAYMENT_MODE, DURATION, ATTENDANT_CLOCKED_IN_DATE) VALUES (:id, :studentName, :attendantName, :activity, :cost, :paymentMode, :duration, :clockedInDate)", nativeQuery = true)
    void upsertShiftTotal(@Param("id") UUID id,
                          @Param("studentName") String studentName,
                          @Param("attendantName") String attendantName,
                          @Param("activity") String activity,
                          @Param("cost") String cost,
                          @Param("paymentMode") String paymentMode,
                          @Param("duration") String duration,
                          @Param("clockedInDate") Date clockedInDate);

    // Update individual fields
    @Modifying
    @Transactional
    @Query(value = "UPDATE SHIFT_TOTAL SET STUDENT_NAME = :studentName WHERE ID = :id", nativeQuery = true)
    void updateStudentName(@Param("id") UUID id, @Param("studentName") String studentName);

    @Modifying
    @Transactional
    @Query(value = "UPDATE SHIFT_TOTAL SET ATTENDANT_NAME = :attendantName WHERE ID = :id", nativeQuery = true)
    void updateAttendantName(@Param("id") UUID id, @Param("attendantName") String attendantName);

    @Modifying
    @Transactional
    @Query(value = "UPDATE SHIFT_TOTAL SET ACTIVITY = :activity WHERE ID = :id", nativeQuery = true)
    void updateActivity(@Param("id") UUID id, @Param("activity") String activity);

    @Modifying
    @Transactional
    @Query(value = "UPDATE SHIFT_TOTAL SET COST = :cost WHERE ID = :id", nativeQuery = true)
    void updateCost(@Param("id") UUID id, @Param("cost") String cost);

    @Modifying
    @Transactional
    @Query(value = "UPDATE SHIFT_TOTAL SET PAYMENT_MODE = :paymentMode WHERE ID = :id", nativeQuery = true)
    void updatePaymentMode(@Param("id") UUID id, @Param("paymentMode") String paymentMode);

    @Modifying
    @Transactional
    @Query(value = "UPDATE SHIFT_TOTAL SET DURATION = :duration WHERE ID = :id", nativeQuery = true)
    void updateDuration(@Param("id") UUID id, @Param("duration") String duration);

    @Query(value = "UPDATE SHIFT_TOTAL SET ATTENDANT_CLOCKED_IN_DATE = :clockedInDate WHERE ID = :id", nativeQuery = true)
    void updateClockedInDate(@Param("id") UUID id, @Param("clockedInDate") Date clockedInDate);

    // Delete a shift total
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM SHIFT_TOTAL WHERE ID = :id", nativeQuery = true)
    void deleteShiftTotal(@Param("id") UUID id);
}
