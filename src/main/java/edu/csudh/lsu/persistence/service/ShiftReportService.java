package edu.csudh.lsu.persistence.service;

import edu.csudh.lsu.persistence.constants.PersistenceConstants;
import edu.csudh.lsu.persistence.exception.PersistenceException;
import edu.csudh.lsu.persistence.model.shift.ShiftReport;
import edu.csudh.lsu.persistence.repository.gamesroom.shift.ShiftReportRepository;
import edu.csudh.lsu.persistence.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.TransactionException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.util.UUID;

/**
 * <p>
 * Shift Report Service class responsible for CRUD operation on Shift Report Table.
 * </p>
 *
 * <p>
 * Created by: digvijay
 * Date: 8/6/24
 * </p>
 *
 * <p>
 * Author: Digvijay Hethur Jagadeesha
 * </p>
 *
 * <p>
 * All Rights Reserved by Loker Student Union Inc at California State University, Dominguez Hills from 2024.
 * </p>
 */

@Slf4j
@Service
public class ShiftReportService {

    @Autowired
    private ShiftReportRepository shiftReportRepository;

    /**
     * Saves or updates the provided ShiftReport entity.
     *
     * @param shiftReport The ShiftReport entity to be saved or updated.
     * @throws TransactionException                  if a transaction error occurs during the operation.
     * @throws JDBCConnectionException               if there is a JDBC connection issue.
     * @throws JpaSystemException                    if there is a JPA system error.
     * @throws DataAccessResourceFailureException    if a data access resource fails.
     * @throws PersistenceException                  if a general persistence error occurs.
     */
    public void saveOrUpdateShiftReport(ShiftReport shiftReport) throws TransactionException, JDBCConnectionException, JpaSystemException
            , DataAccessResourceFailureException {

        try {
            log.debug("Attempting to save or update shift report: {}", shiftReport);
            if (shiftReport != null) {
                var currentTime = TimeUtils.getFormattedCurrentPSTTime();
                Date currentDate = new Date(currentTime.getTime());

//                shiftReport.setCreatedTime(currentTime);
                shiftReport.setCreatedDate(currentDate);
                shiftReport.setLastUpdatedDate(currentDate);
//                shiftReport.setLastUpdatedTime(currentTime);

                shiftReportRepository.upsertShiftReport(
                        shiftReport.getClosingShiftDate(), shiftReport.getClosingShiftTime(), shiftReport.getAttendantName(),
                        shiftReport.getReconcilorName(), shiftReport.getReconcilorSign(), shiftReport.getAttendantSign(),
                        shiftReport.getRevenueInCard(), shiftReport.getRevenueInCash(), shiftReport.getShiftTotal(),
                        shiftReport.getOpeningBalance(), shiftReport.getCreatedTime(), shiftReport.getCreatedDate(),
                        shiftReport.getLastUpdatedDate(), shiftReport.getLastUpdatedTime(),
                        shiftReport.getLastUpdatedBy(), shiftReport.getAccessedBy()
                );

                log.info("Shift report saved or updated successfully.");
            } else {
                log.warn("Attempted to save or update a null shift report.");
            }
        } catch (DataAccessResourceFailureException | JDBCConnectionException | JpaSystemException
                 | TransactionException exception) {
            log.error("Data access or transaction failure while saving or updating shift report.", exception);
            throw exception;
        } catch (Exception exception) {
            log.error("An unexpected error occurred while saving or updating shift report.", exception);
            throw new PersistenceException(PersistenceConstants.AN_EXCEPTION_OCCURRED_WHILE_UPSERTING_A_RECORD, exception.getMessage());
        }
    }

    /**
     * Updates the closing shift date for a ShiftReport record.
     *
     * @param shiftReportId  The ID of the ShiftReport record to update.
     * @param closingShiftDate The new closing shift date.
     * @param lastUpdatedBy  The user who last updated the record.
     * @param accessedBy     The user who accessed the record.
     * @throws TransactionException                  if a transaction error occurs during the operation.
     * @throws JDBCConnectionException               if there is a JDBC connection issue.
     * @throws JpaSystemException                    if there is a JPA system error.
     * @throws DataAccessResourceFailureException    if a data access resource fails.
     * @throws PersistenceException                  if a general persistence error occurs.
     */
    public void updateClosingShiftDate(UUID shiftReportId, Date closingShiftDate, String lastUpdatedBy, String accessedBy)
            throws TransactionException, JDBCConnectionException, JpaSystemException, DataAccessResourceFailureException {

        try {
            log.debug("Attempting to update closing shift date for ShiftReport ID: {}", shiftReportId);
            Date currentDate = new Date(TimeUtils.getFormattedCurrentPSTTime().getTime());
            Time currentTime = new Time(TimeUtils.getFormattedCurrentPSTTime().getTime());

            shiftReportRepository.updateClosingShiftDate(shiftReportId, closingShiftDate, currentDate, currentTime, lastUpdatedBy, accessedBy);
            log.info("Closing shift date updated successfully for ShiftReport ID: {}", shiftReportId);
        } catch (DataAccessResourceFailureException | JDBCConnectionException | JpaSystemException
                 | TransactionException exception) {
            log.error("Data access or transaction failure while updating closing shift date for ShiftReport ID: {}", shiftReportId, exception);
            throw exception;
        } catch (Exception exception) {
            log.error("An unexpected error occurred while updating closing shift date for ShiftReport ID: {}", shiftReportId, exception);
            throw new PersistenceException(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
        }
    }

    /**
     * Updates the closing shift time for a ShiftReport record.
     *
     * @param shiftReportId  The ID of the ShiftReport record to update.
     * @param closingShiftTime The new closing shift time.
     * @param lastUpdatedBy  The user who last updated the record.
     * @param accessedBy     The user who accessed the record.
     * @throws TransactionException                  if a transaction error occurs during the operation.
     * @throws JDBCConnectionException               if there is a JDBC connection issue.
     * @throws JpaSystemException                    if there is a JPA system error.
     * @throws DataAccessResourceFailureException    if a data access resource fails.
     * @throws PersistenceException                  if a general persistence error occurs.
     */
    public void updateClosingShiftTime(UUID shiftReportId, Time closingShiftTime, String lastUpdatedBy, String accessedBy)
            throws TransactionException, JDBCConnectionException, JpaSystemException, DataAccessResourceFailureException {

        try {
            log.debug("Attempting to update closing shift time for ShiftReport ID: {}", shiftReportId);
            Date currentDate = new Date(TimeUtils.getFormattedCurrentPSTTime().getTime());
            Time currentTime = new Time(TimeUtils.getFormattedCurrentPSTTime().getTime());

            shiftReportRepository.updateClosingShiftTime(shiftReportId, closingShiftTime, currentDate, currentTime, lastUpdatedBy, accessedBy);
            log.info("Closing shift time updated successfully for ShiftReport ID: {}", shiftReportId);
        } catch (DataAccessResourceFailureException | JDBCConnectionException | JpaSystemException
                 | TransactionException exception) {
            log.error("Data access or transaction failure while updating closing shift time for ShiftReport ID: {}", shiftReportId, exception);
            throw exception;
        } catch (Exception exception) {
            log.error("An unexpected error occurred while updating closing shift time for ShiftReport ID: {}", shiftReportId, exception);
            throw new PersistenceException(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
        }
    }

    // Similar methods can be created for updateAttendantName, updateReconcilorName, updateReconcilorSign, updateAttendantSign, updateRevenueInCard, updateRevenueInCash, updateShiftTotal, updateOpeningBalance following the same pattern.

    /**
     * Deletes a ShiftReport record by its ID.
     *
     * @param shiftReportId The ID of the ShiftReport record to delete.
     * @throws TransactionException                  if a transaction error occurs during the operation.
     * @throws JDBCConnectionException               if there is a JDBC connection issue.
     * @throws JpaSystemException                    if there is a JPA system error.
     * @throws DataAccessResourceFailureException    if a data access resource fails.
     * @throws PersistenceException                  if a general persistence error occurs.
     */
    public void deleteShiftReport(UUID shiftReportId) throws TransactionException, JDBCConnectionException, JpaSystemException, DataAccessResourceFailureException {

        try {
            log.debug("Attempting to delete ShiftReport with ID: {}", shiftReportId);
            shiftReportRepository.deleteShiftReport(shiftReportId);
            log.info("ShiftReport deleted successfully with ID: {}", shiftReportId);
        } catch (DataAccessResourceFailureException | JDBCConnectionException | JpaSystemException
                 | TransactionException exception) {
            log.error("Data access or transaction failure while deleting ShiftReport with ID: {}", shiftReportId, exception);
            throw exception;
        } catch (Exception exception) {
            log.error("An unexpected error occurred while deleting ShiftReport with ID: {}", shiftReportId, exception);
            throw new PersistenceException(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
        }
    }

    /**
     * Fetches all ShiftReport records from the repository and returns them in a paginated format.
     *
     * @param pageable Pagination information.
     * @return A paginated list of ShiftReport records.
     * @throws TransactionException                  if a transaction error occurs during the operation.
     * @throws JDBCConnectionException               if there is a JDBC connection issue.
     * @throws JpaSystemException                    if there is a JPA system error.
     * @throws DataAccessResourceFailureException    if a data access resource fails.
     * @throws PersistenceException                  if a general persistence error occurs.
     */
    public Page<ShiftReport> fetchAllShiftReports(Pageable pageable) throws TransactionException, JDBCConnectionException, JpaSystemException
            , DataAccessResourceFailureException {

        try {
            log.debug("Attempting to fetch all ShiftReport records");
            Page<ShiftReport> shiftReports = shiftReportRepository.findAll(pageable);
            log.info("Fetched {} ShiftReport records.", shiftReports.getTotalElements());
            return shiftReports;
        } catch (DataAccessResourceFailureException | JDBCConnectionException | JpaSystemException
                 | TransactionException exception) {
            log.error("Data access or transaction failure while fetching ShiftReport records.", exception);
            throw exception;
        } catch (Exception exception) {
            log.error("An unexpected error occurred while fetching ShiftReport records.", exception);
            throw new PersistenceException(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
        }
    }
}
