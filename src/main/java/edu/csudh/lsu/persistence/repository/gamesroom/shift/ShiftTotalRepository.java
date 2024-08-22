package edu.csudh.lsu.persistence.repository.gamesroom.shift;

import edu.csudh.lsu.persistence.model.shift.ShiftTotal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.util.UUID;

@Repository
public interface ShiftTotalRepository extends JpaRepository<ShiftTotal, UUID> {

    // Upsert a shift total (Insert or Update)
    @Modifying
    @Transactional
    @Query(value = "UPSERT INTO SHIFT_TOTAL (STUDENT_NAME, ATTENDANT_NAME, ACTIVITY, COST, PAYMENT_MODE, START_TIME, DATE, DURATION, CREATED_TIME, CREATED_DATE, LAST_UPDATED_DATE, LAST_UPDATED_TIME, LAST_UPDATED_BY, ACCESSED_BY) " +
            "VALUES (:studentName, :attendantName, :activity, :cost, :paymentMode, :startTime, :date, :duration, :createdTime, :createdDate, :lastUpdatedDate, :lastUpdatedTime, :lastUpdatedBy, :accessedBy)", nativeQuery = true)
    void upsertShiftTotal(@Param("studentName") String studentName,
                          @Param("attendantName") String attendantName,
                          @Param("activity") String activity,
                          @Param("cost") Float cost,
                          @Param("paymentMode") String paymentMode,
                          @Param("startTime") Time startTime,
                          @Param("date") Date date,
                          @Param("duration") String duration,
                          @Param("createdTime") Time createdTime,
                          @Param("createdDate") Date createdDate,
                          @Param("lastUpdatedDate") Date lastUpdatedDate,
                          @Param("lastUpdatedTime") Time lastUpdatedTime,
                          @Param("lastUpdatedBy") String lastUpdatedBy,
                          @Param("accessedBy") String accessedBy);

    // Update the student name
    @Modifying
    @Transactional
    @Query(value = "UPDATE SHIFT_TOTAL SET STUDENT_NAME = :studentName, LAST_UPDATED_DATE = :lastUpdatedDate, LAST_UPDATED_TIME = :lastUpdatedTime, LAST_UPDATED_BY = :lastUpdatedBy, ACCESSED_BY = :accessedBy WHERE ID = :id", nativeQuery = true)
    void updateStudentName(@Param("id") UUID id, @Param("studentName") String studentName,
                           @Param("lastUpdatedDate") Date lastUpdatedDate, @Param("lastUpdatedTime") Time lastUpdatedTime,
                           @Param("lastUpdatedBy") String lastUpdatedBy, @Param("accessedBy") String accessedBy);

    // Update the attendant name
    @Modifying
    @Transactional
    @Query(value = "UPDATE SHIFT_TOTAL SET ATTENDANT_NAME = :attendantName, LAST_UPDATED_DATE = :lastUpdatedDate, LAST_UPDATED_TIME = :lastUpdatedTime, LAST_UPDATED_BY = :lastUpdatedBy, ACCESSED_BY = :accessedBy WHERE ID = :id", nativeQuery = true)
    void updateAttendantName(@Param("id") UUID id, @Param("attendantName") String attendantName,
                             @Param("lastUpdatedDate") Date lastUpdatedDate, @Param("lastUpdatedTime") Time lastUpdatedTime,
                             @Param("lastUpdatedBy") String lastUpdatedBy, @Param("accessedBy") String accessedBy);

    // Update the activity
    @Modifying
    @Transactional
    @Query(value = "UPDATE SHIFT_TOTAL SET ACTIVITY = :activity, LAST_UPDATED_DATE = :lastUpdatedDate, LAST_UPDATED_TIME = :lastUpdatedTime, LAST_UPDATED_BY = :lastUpdatedBy, ACCESSED_BY = :accessedBy WHERE ID = :id", nativeQuery = true)
    void updateActivity(@Param("id") UUID id, @Param("activity") String activity,
                        @Param("lastUpdatedDate") Date lastUpdatedDate, @Param("lastUpdatedTime") Time lastUpdatedTime,
                        @Param("lastUpdatedBy") String lastUpdatedBy, @Param("accessedBy") String accessedBy);

    // Update the cost
    @Modifying
    @Transactional
    @Query(value = "UPDATE SHIFT_TOTAL SET COST = :cost, LAST_UPDATED_DATE = :lastUpdatedDate, LAST_UPDATED_TIME = :lastUpdatedTime, LAST_UPDATED_BY = :lastUpdatedBy, ACCESSED_BY = :accessedBy WHERE ID = :id", nativeQuery = true)
    void updateCost(@Param("id") UUID id, @Param("cost") Float cost,
                    @Param("lastUpdatedDate") Date lastUpdatedDate, @Param("lastUpdatedTime") Time lastUpdatedTime,
                    @Param("lastUpdatedBy") String lastUpdatedBy, @Param("accessedBy") String accessedBy);

    // Update the payment mode
    @Modifying
    @Transactional
    @Query(value = "UPDATE SHIFT_TOTAL SET PAYMENT_MODE = :paymentMode, LAST_UPDATED_DATE = :lastUpdatedDate, LAST_UPDATED_TIME = :lastUpdatedTime, LAST_UPDATED_BY = :lastUpdatedBy, ACCESSED_BY = :accessedBy WHERE ID = :id", nativeQuery = true)
    void updatePaymentMode(@Param("id") UUID id, @Param("paymentMode") String paymentMode,
                           @Param("lastUpdatedDate") Date lastUpdatedDate, @Param("lastUpdatedTime") Time lastUpdatedTime,
                           @Param("lastUpdatedBy") String lastUpdatedBy, @Param("accessedBy") String accessedBy);

    // Update the duration
    @Modifying
    @Transactional
    @Query(value = "UPDATE SHIFT_TOTAL SET DURATION = :duration, LAST_UPDATED_DATE = :lastUpdatedDate, LAST_UPDATED_TIME = :lastUpdatedTime, LAST_UPDATED_BY = :lastUpdatedBy, ACCESSED_BY = :accessedBy WHERE ID = :id", nativeQuery = true)
    void updateDuration(@Param("id") UUID id, @Param("duration") String duration,
                        @Param("lastUpdatedDate") Date lastUpdatedDate, @Param("lastUpdatedTime") Time lastUpdatedTime,
                        @Param("lastUpdatedBy") String lastUpdatedBy, @Param("accessedBy") String accessedBy);

    // Delete a shift total
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM SHIFT_TOTAL WHERE ID = :id", nativeQuery = true)
    void deleteShiftTotal(@Param("id") UUID id);

    // Calculate total costs for a given attendant for a specific date
    @Query(value = "SELECT " +
            "attendant_name, " +
            "SUM(CASE WHEN payment_mode = 'card' THEN CAST(cost AS DECIMAL(10,2)) ELSE 0 END) AS total_cost_card, " +
            "SUM(CASE WHEN payment_mode = 'cash' THEN CAST(cost AS DECIMAL(10,2)) ELSE 0 END) AS total_cost_cash, " +
            "SUM(CAST(cost AS DECIMAL(10,2))) AS total_cost " +
            "FROM SHIFT_TOTAL " +
            "WHERE DATE = :date " +
            "AND attendant_name = :attendantName " +
            "GROUP BY attendant_name", nativeQuery = true)
    Object findTotalCostsByAttendantNameAndDate(@Param("attendantName") String attendantName, @Param("date") Date date);

}
