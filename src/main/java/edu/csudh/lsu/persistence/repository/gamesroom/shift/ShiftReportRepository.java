package edu.csudh.lsu.persistence.repository.gamesroom.shift;

import edu.csudh.lsu.persistence.model.shift.ShiftReport;
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
public interface ShiftReportRepository extends JpaRepository<ShiftReport, UUID> {

    // Upsert a shift report
    @Modifying
    @Transactional
    @Query(value = "UPSERT INTO SHIFT_REPORT (CLOSING_SHIFT_DATE, CLOSING_SHIFT_TIME, ATTENDANT_NAME, RECONCILOR_NAME, RECONCILOR_SIGN, ATTENDANT_SIGN, REVENUE_IN_CARD, REVENUE_IN_CASH, SHIFT_TOTAL, OPENING_BALANCE, CREATED_TIME, CREATED_DATE, LAST_UPDATED_DATE, LAST_UPDATED_TIME, LAST_UPDATED_BY, ACCESSED_BY) " +
            "VALUES (:closingShiftDate, :closingShiftTime, :attendantName, :reconcilorName, :reconcilorSign, :attendantSign, :revenueInCard, :revenueInCash, :shiftTotal, :openingBalance, :createdTime, :createdDate, :lastUpdatedDate, :lastUpdatedTime, :lastUpdatedBy, :accessedBy)", nativeQuery = true)
    void upsertShiftReport(@Param("closingShiftDate") Date closingShiftDate,
                           @Param("closingShiftTime") Time closingShiftTime,
                           @Param("attendantName") String attendantName,
                           @Param("reconcilorName") String reconcilorName,
                           @Param("reconcilorSign") String reconcilorSign,
                           @Param("attendantSign") String attendantSign,
                           @Param("revenueInCard") Float revenueInCard,
                           @Param("revenueInCash") Float revenueInCash,
                           @Param("shiftTotal") String shiftTotal,
                           @Param("openingBalance") Float openingBalance,
                           @Param("createdTime") Time createdTime,
                           @Param("createdDate") Date createdDate,
                           @Param("lastUpdatedDate") Date lastUpdatedDate,
                           @Param("lastUpdatedTime") Time lastUpdatedTime,
                           @Param("lastUpdatedBy") String lastUpdatedBy,
                           @Param("accessedBy") String accessedBy);

    // Update the closing shift date
    @Modifying
    @Transactional
    @Query(value = "UPDATE SHIFT_REPORT SET CLOSING_SHIFT_DATE = :closingShiftDate, LAST_UPDATED_DATE = :lastUpdatedDate, LAST_UPDATED_TIME = :lastUpdatedTime, LAST_UPDATED_BY = :lastUpdatedBy, ACCESSED_BY = :accessedBy WHERE SHIFT_REPORT_ID = :shiftReportId", nativeQuery = true)
    void updateClosingShiftDate(@Param("shiftReportId") UUID shiftReportId,
                                @Param("closingShiftDate") Date closingShiftDate,
                                @Param("lastUpdatedDate") Date lastUpdatedDate,
                                @Param("lastUpdatedTime") Time lastUpdatedTime,
                                @Param("lastUpdatedBy") String lastUpdatedBy,
                                @Param("accessedBy") String accessedBy);

    // Update the closing shift time
    @Modifying
    @Transactional
    @Query(value = "UPDATE SHIFT_REPORT SET CLOSING_SHIFT_TIME = :closingShiftTime, LAST_UPDATED_DATE = :lastUpdatedDate, LAST_UPDATED_TIME = :lastUpdatedTime, LAST_UPDATED_BY = :lastUpdatedBy, ACCESSED_BY = :accessedBy WHERE SHIFT_REPORT_ID = :shiftReportId", nativeQuery = true)
    void updateClosingShiftTime(@Param("shiftReportId") UUID shiftReportId,
                                @Param("closingShiftTime") Time closingShiftTime,
                                @Param("lastUpdatedDate") Date lastUpdatedDate,
                                @Param("lastUpdatedTime") Time lastUpdatedTime,
                                @Param("lastUpdatedBy") String lastUpdatedBy,
                                @Param("accessedBy") String accessedBy);

    // Update the attendant name
    @Modifying
    @Transactional
    @Query(value = "UPDATE SHIFT_REPORT SET ATTENDANT_NAME = :attendantName, LAST_UPDATED_DATE = :lastUpdatedDate, LAST_UPDATED_TIME = :lastUpdatedTime, LAST_UPDATED_BY = :lastUpdatedBy, ACCESSED_BY = :accessedBy WHERE SHIFT_REPORT_ID = :shiftReportId", nativeQuery = true)
    void updateAttendantName(@Param("shiftReportId") UUID shiftReportId,
                             @Param("attendantName") String attendantName,
                             @Param("lastUpdatedDate") Date lastUpdatedDate,
                             @Param("lastUpdatedTime") Time lastUpdatedTime,
                             @Param("lastUpdatedBy") String lastUpdatedBy,
                             @Param("accessedBy") String accessedBy);

    // Update the reconcilor name
    @Modifying
    @Transactional
    @Query(value = "UPDATE SHIFT_REPORT SET RECONCILOR_NAME = :reconcilorName, LAST_UPDATED_DATE = :lastUpdatedDate, LAST_UPDATED_TIME = :lastUpdatedTime, LAST_UPDATED_BY = :lastUpdatedBy, ACCESSED_BY = :accessedBy WHERE SHIFT_REPORT_ID = :shiftReportId", nativeQuery = true)
    void updateReconcilorName(@Param("shiftReportId") UUID shiftReportId,
                              @Param("reconcilorName") String reconcilorName,
                              @Param("lastUpdatedDate") Date lastUpdatedDate,
                              @Param("lastUpdatedTime") Time lastUpdatedTime,
                              @Param("lastUpdatedBy") String lastUpdatedBy,
                              @Param("accessedBy") String accessedBy);

    // Update the reconcilor sign
    @Modifying
    @Transactional
    @Query(value = "UPDATE SHIFT_REPORT SET RECONCILOR_SIGN = :reconcilorSign, LAST_UPDATED_DATE = :lastUpdatedDate, LAST_UPDATED_TIME = :lastUpdatedTime, LAST_UPDATED_BY = :lastUpdatedBy, ACCESSED_BY = :accessedBy WHERE SHIFT_REPORT_ID = :shiftReportId", nativeQuery = true)
    void updateReconcilorSign(@Param("shiftReportId") UUID shiftReportId,
                              @Param("reconcilorSign") String reconcilorSign,
                              @Param("lastUpdatedDate") Date lastUpdatedDate,
                              @Param("lastUpdatedTime") Time lastUpdatedTime,
                              @Param("lastUpdatedBy") String lastUpdatedBy,
                              @Param("accessedBy") String accessedBy);

    // Update the attendant sign
    @Modifying
    @Transactional
    @Query(value = "UPDATE SHIFT_REPORT SET ATTENDANT_SIGN = :attendantSign, LAST_UPDATED_DATE = :lastUpdatedDate, LAST_UPDATED_TIME = :lastUpdatedTime, LAST_UPDATED_BY = :lastUpdatedBy, ACCESSED_BY = :accessedBy WHERE SHIFT_REPORT_ID = :shiftReportId", nativeQuery = true)
    void updateAttendantSign(@Param("shiftReportId") UUID shiftReportId,
                             @Param("attendantSign") String attendantSign,
                             @Param("lastUpdatedDate") Date lastUpdatedDate,
                             @Param("lastUpdatedTime") Time lastUpdatedTime,
                             @Param("lastUpdatedBy") String lastUpdatedBy,
                             @Param("accessedBy") String accessedBy);

    // Update the revenue in card
    @Modifying
    @Transactional
    @Query(value = "UPDATE SHIFT_REPORT SET REVENUE_IN_CARD = :revenueInCard, LAST_UPDATED_DATE = :lastUpdatedDate, LAST_UPDATED_TIME = :lastUpdatedTime, LAST_UPDATED_BY = :lastUpdatedBy, ACCESSED_BY = :accessedBy WHERE SHIFT_REPORT_ID = :shiftReportId", nativeQuery = true)
    void updateRevenueInCard(@Param("shiftReportId") UUID shiftReportId,
                             @Param("revenueInCard") Float revenueInCard,
                             @Param("lastUpdatedDate") Date lastUpdatedDate,
                             @Param("lastUpdatedTime") Time lastUpdatedTime,
                             @Param("lastUpdatedBy") String lastUpdatedBy,
                             @Param("accessedBy") String accessedBy);

    // Update the revenue in cash
    @Modifying
    @Transactional
    @Query(value = "UPDATE SHIFT_REPORT SET REVENUE_IN_CASH = :revenueInCash, LAST_UPDATED_DATE = :lastUpdatedDate, LAST_UPDATED_TIME = :lastUpdatedTime, LAST_UPDATED_BY = :lastUpdatedBy, ACCESSED_BY = :accessedBy WHERE SHIFT_REPORT_ID = :shiftReportId", nativeQuery = true)
    void updateRevenueInCash(@Param("shiftReportId") UUID shiftReportId,
                             @Param("revenueInCash") Float revenueInCash,
                             @Param("lastUpdatedDate") Date lastUpdatedDate,
                             @Param("lastUpdatedTime") Time lastUpdatedTime,
                             @Param("lastUpdatedBy") String lastUpdatedBy,
                             @Param("accessedBy") String accessedBy);

    // Update the shift total
    @Modifying
    @Transactional
    @Query(value = "UPDATE SHIFT_REPORT SET SHIFT_TOTAL = :shiftTotal, LAST_UPDATED_DATE = :lastUpdatedDate, LAST_UPDATED_TIME = :lastUpdatedTime, LAST_UPDATED_BY = :lastUpdatedBy, ACCESSED_BY = :accessedBy WHERE SHIFT_REPORT_ID = :shiftReportId", nativeQuery = true)
    void updateShiftTotal(@Param("shiftReportId") UUID shiftReportId,
                          @Param("shiftTotal") String shiftTotal,
                          @Param("lastUpdatedDate") Date lastUpdatedDate,
                          @Param("lastUpdatedTime") Time lastUpdatedTime,
                          @Param("lastUpdatedBy") String lastUpdatedBy,
                          @Param("accessedBy") String accessedBy);

    // Update the opening balance
    @Modifying
    @Transactional
    @Query(value = "UPDATE SHIFT_REPORT SET OPENING_BALANCE = :openingBalance, LAST_UPDATED_DATE = :lastUpdatedDate, LAST_UPDATED_TIME = :lastUpdatedTime, LAST_UPDATED_BY = :lastUpdatedBy, ACCESSED_BY = :accessedBy WHERE SHIFT_REPORT_ID = :shiftReportId", nativeQuery = true)
    void updateOpeningBalance(@Param("shiftReportId") UUID shiftReportId,
                              @Param("openingBalance") Float openingBalance,
                              @Param("lastUpdatedDate") Date lastUpdatedDate,
                              @Param("lastUpdatedTime") Time lastUpdatedTime,
                              @Param("lastUpdatedBy") String lastUpdatedBy,
                              @Param("accessedBy") String accessedBy);

    // Delete a shift report
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM SHIFT_REPORT WHERE SHIFT_REPORT_ID = :shiftReportId", nativeQuery = true)
    void deleteShiftReport(@Param("shiftReportId") UUID shiftReportId);

}
