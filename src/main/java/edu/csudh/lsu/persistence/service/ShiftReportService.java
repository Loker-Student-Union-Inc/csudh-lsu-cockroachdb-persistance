package edu.csudh.lsu.persistence.service;

import edu.csudh.lsu.persistence.constants.PersistenceConstants;
import edu.csudh.lsu.persistence.exception.PersistenceException;
import edu.csudh.lsu.persistence.model.shift.ShiftReport;
import edu.csudh.lsu.persistence.repository.gamesroom.shift.ShiftReportRepository;
import edu.csudh.lsu.persistence.utils.PersistenceStringUtils;
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

                shiftReport.setCreatedTime(new Time(TimeUtils.getFormattedCurrentPSTTime().getTime()));
                shiftReport.setCreatedDate(currentDate);
                shiftReport.setLastUpdatedDate(currentDate);
                shiftReport.setLastUpdatedTime(new Time(TimeUtils.getFormattedCurrentPSTTime().getTime()));

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
     * Updates the attendant name for a ShiftReport record.
     *
     * @param shiftReportId  The ID of the ShiftReport record to update.
     * @param attendantName  The new attendant name.
     * @param lastUpdatedBy  The user who last updated the record.
     * @param accessedBy     The user who accessed the record.
     * @throws TransactionException                  if a transaction error occurs during the operation.
     * @throws JDBCConnectionException               if there is a JDBC connection issue.
     * @throws JpaSystemException                    if there is a JPA system error.
     * @throws DataAccessResourceFailureException    if a data access resource fails.
     * @throws PersistenceException                  if a general persistence error occurs.
     */
    public void updateAttendantName(UUID shiftReportId, String attendantName, String lastUpdatedBy, String accessedBy)
            throws TransactionException, JDBCConnectionException, JpaSystemException, DataAccessResourceFailureException {

        try {
            if (PersistenceStringUtils.isNotNullOrEmpty(attendantName)) {
                log.debug("Attempting to update attendant name for ShiftReport ID: {}", shiftReportId);
                Date currentDate = new Date(TimeUtils.getFormattedCurrentPSTTime().getTime());
                Time currentTime = new Time(TimeUtils.getFormattedCurrentPSTTime().getTime());

                shiftReportRepository.updateAttendantName(shiftReportId, attendantName, currentDate, currentTime, lastUpdatedBy, accessedBy);
                log.info("Attendant name updated successfully for ShiftReport ID: {}", shiftReportId);
            } else {
                log.warn("Attendant name is null or empty. Update aborted for ShiftReport ID: {}", shiftReportId);
            }
        } catch (DataAccessResourceFailureException | JDBCConnectionException | JpaSystemException
                 | TransactionException exception) {
            log.error("Data access or transaction failure while updating attendant name for ShiftReport ID: {}", shiftReportId, exception);
            throw exception;
        } catch (Exception exception) {
            log.error("An unexpected error occurred while updating attendant name for ShiftReport ID: {}", shiftReportId, exception);
            throw new PersistenceException(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
        }
    }

    /**
     * Updates the reconcilor name for a ShiftReport record.
     *
     * @param shiftReportId  The ID of the ShiftReport record to update.
     * @param reconcilorName The new reconcilor name.
     * @param lastUpdatedBy  The user who last updated the record.
     * @param accessedBy     The user who accessed the record.
     * @throws TransactionException                  if a transaction error occurs during the operation.
     * @throws JDBCConnectionException               if there is a JDBC connection issue.
     * @throws JpaSystemException                    if there is a JPA system error.
     * @throws DataAccessResourceFailureException    if a data access resource fails.
     * @throws PersistenceException                  if a general persistence error occurs.
     */
    public void updateReconcilorName(UUID shiftReportId, String reconcilorName, String lastUpdatedBy, String accessedBy)
            throws TransactionException, JDBCConnectionException, JpaSystemException, DataAccessResourceFailureException {

        try {
            if (PersistenceStringUtils.isNotNullOrEmpty(reconcilorName)) {
                log.debug("Attempting to update reconcilor name for ShiftReport ID: {}", shiftReportId);
                Date currentDate = new Date(TimeUtils.getFormattedCurrentPSTTime().getTime());
                Time currentTime = new Time(TimeUtils.getFormattedCurrentPSTTime().getTime());

                shiftReportRepository.updateReconcilorName(shiftReportId, reconcilorName, currentDate, currentTime, lastUpdatedBy, accessedBy);
                log.info("Reconcilor name updated successfully for ShiftReport ID: {}", shiftReportId);
            } else {
                log.warn("Reconcilor name is null or empty. Update aborted for ShiftReport ID: {}", shiftReportId);
            }
        } catch (DataAccessResourceFailureException | JDBCConnectionException | JpaSystemException
                 | TransactionException exception) {
            log.error("Data access or transaction failure while updating reconcilor name for ShiftReport ID: {}", shiftReportId, exception);
            throw exception;
        } catch (Exception exception) {
            log.error("An unexpected error occurred while updating reconcilor name for ShiftReport ID: {}", shiftReportId, exception);
            throw new PersistenceException(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
        }
    }

    /**
     * Updates the reconcilor sign for a ShiftReport record.
     *
     * @param shiftReportId  The ID of the ShiftReport record to update.
     * @param reconcilorSign The new reconcilor sign.
     * @param lastUpdatedBy  The user who last updated the record.
     * @param accessedBy     The user who accessed the record.
     * @throws TransactionException                  if a transaction error occurs during the operation.
     * @throws JDBCConnectionException               if there is a JDBC connection issue.
     * @throws JpaSystemException                    if there is a JPA system error.
     * @throws DataAccessResourceFailureException    if a data access resource fails.
     * @throws PersistenceException                  if a general persistence error occurs.
     */
    public void updateReconcilorSign(UUID shiftReportId, String reconcilorSign, String lastUpdatedBy, String accessedBy)
            throws TransactionException, JDBCConnectionException, JpaSystemException, DataAccessResourceFailureException {

        try {
            if (PersistenceStringUtils.isNotNullOrEmpty(reconcilorSign)) {
                log.debug("Attempting to update reconcilor sign for ShiftReport ID: {}", shiftReportId);
                Date currentDate = new Date(TimeUtils.getFormattedCurrentPSTTime().getTime());
                Time currentTime = new Time(TimeUtils.getFormattedCurrentPSTTime().getTime());

                shiftReportRepository.updateReconcilorSign(shiftReportId, reconcilorSign, currentDate, currentTime, lastUpdatedBy, accessedBy);
                log.info("Reconcilor sign updated successfully for ShiftReport ID: {}", shiftReportId);
            } else {
                log.warn("Reconcilor sign is null or empty. Update aborted for ShiftReport ID: {}", shiftReportId);
            }
        } catch (DataAccessResourceFailureException | JDBCConnectionException | JpaSystemException
                 | TransactionException exception) {
            log.error("Data access or transaction failure while updating reconcilor sign for ShiftReport ID: {}", shiftReportId, exception);
            throw exception;
        } catch (Exception exception) {
            log.error("An unexpected error occurred while updating reconcilor sign for ShiftReport ID: {}", shiftReportId, exception);
            throw new PersistenceException(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
        }
    }

    /**
     * Updates the attendant sign for a ShiftReport record.
     *
     * @param shiftReportId  The ID of the ShiftReport record to update.
     * @param attendantSign  The new attendant sign.
     * @param lastUpdatedBy  The user who last updated the record.
     * @param accessedBy     The user who accessed the record.
     * @throws TransactionException                  if a transaction error occurs during the operation.
     * @throws JDBCConnectionException               if there is a JDBC connection issue.
     * @throws JpaSystemException                    if there is a JPA system error.
     * @throws DataAccessResourceFailureException    if a data access resource fails.
     * @throws PersistenceException                  if a general persistence error occurs.
     */
    public void updateAttendantSign(UUID shiftReportId, String attendantSign, String lastUpdatedBy, String accessedBy)
            throws TransactionException, JDBCConnectionException, JpaSystemException, DataAccessResourceFailureException {

        try {
            if (PersistenceStringUtils.isNotNullOrEmpty(attendantSign)) {
                log.debug("Attempting to update attendant sign for ShiftReport ID: {}", shiftReportId);
                Date currentDate = new Date(TimeUtils.getFormattedCurrentPSTTime().getTime());
                Time currentTime = new Time(TimeUtils.getFormattedCurrentPSTTime().getTime());

                shiftReportRepository.updateAttendantSign(shiftReportId, attendantSign, currentDate, currentTime, lastUpdatedBy, accessedBy);
                log.info("Attendant sign updated successfully for ShiftReport ID: {}", shiftReportId);
            } else {
                log.warn("Attendant sign is null or empty. Update aborted for ShiftReport ID: {}", shiftReportId);
            }
        } catch (DataAccessResourceFailureException | JDBCConnectionException | JpaSystemException
                 | TransactionException exception) {
            log.error("Data access or transaction failure while updating attendant sign for ShiftReport ID: {}", shiftReportId, exception);
            throw exception;
        } catch (Exception exception) {
            log.error("An unexpected error occurred while updating attendant sign for ShiftReport ID: {}", shiftReportId, exception);
            throw new PersistenceException(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
        }
    }

    /**
     * Updates the revenue in card for a ShiftReport record.
     *
     * @param shiftReportId  The ID of the ShiftReport record to update.
     * @param revenueInCard  The new revenue in card.
     * @param lastUpdatedBy  The user who last updated the record.
     * @param accessedBy     The user who accessed the record.
     * @throws TransactionException                  if a transaction error occurs during the operation.
     * @throws JDBCConnectionException               if there is a JDBC connection issue.
     * @throws JpaSystemException                    if there is a JPA system error.
     * @throws DataAccessResourceFailureException    if a data access resource fails.
     * @throws PersistenceException                  if a general persistence error occurs.
     */
    public void updateRevenueInCard(UUID shiftReportId, Float revenueInCard, String lastUpdatedBy, String accessedBy)
            throws TransactionException, JDBCConnectionException, JpaSystemException, DataAccessResourceFailureException {

        try {
            log.debug("Attempting to update revenue in card for ShiftReport ID: {}", shiftReportId);
            Date currentDate = new Date(TimeUtils.getFormattedCurrentPSTTime().getTime());
            Time currentTime = new Time(TimeUtils.getFormattedCurrentPSTTime().getTime());

            shiftReportRepository.updateRevenueInCard(shiftReportId, revenueInCard, currentDate, currentTime, lastUpdatedBy, accessedBy);
            log.info("Revenue in card updated successfully for ShiftReport ID: {}", shiftReportId);
        } catch (DataAccessResourceFailureException | JDBCConnectionException | JpaSystemException
                 | TransactionException exception) {
            log.error("Data access or transaction failure while updating revenue in card for ShiftReport ID: {}", shiftReportId, exception);
            throw exception;
        } catch (Exception exception) {
            log.error("An unexpected error occurred while updating revenue in card for ShiftReport ID: {}", shiftReportId, exception);
            throw new PersistenceException(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
        }
    }

    /**
     * Updates the revenue in cash for a ShiftReport record.
     *
     * @param shiftReportId  The ID of the ShiftReport record to update.
     * @param revenueInCash  The new revenue in cash.
     * @param lastUpdatedBy  The user who last updated the record.
     * @param accessedBy     The user who accessed the record.
     * @throws TransactionException                  if a transaction error occurs during the operation.
     * @throws JDBCConnectionException               if there is a JDBC connection issue.
     * @throws JpaSystemException                    if there is a JPA system error.
     * @throws DataAccessResourceFailureException    if a data access resource fails.
     * @throws PersistenceException                  if a general persistence error occurs.
     */
    public void updateRevenueInCash(UUID shiftReportId, Float revenueInCash, String lastUpdatedBy, String accessedBy)
            throws TransactionException, JDBCConnectionException, JpaSystemException, DataAccessResourceFailureException {

        try {
            log.debug("Attempting to update revenue in cash for ShiftReport ID: {}", shiftReportId);
            Date currentDate = new Date(TimeUtils.getFormattedCurrentPSTTime().getTime());
            Time currentTime = new Time(TimeUtils.getFormattedCurrentPSTTime().getTime());

            shiftReportRepository.updateRevenueInCash(shiftReportId, revenueInCash, currentDate, currentTime, lastUpdatedBy, accessedBy);
            log.info("Revenue in cash updated successfully for ShiftReport ID: {}", shiftReportId);
        } catch (DataAccessResourceFailureException | JDBCConnectionException | JpaSystemException
                 | TransactionException exception) {
            log.error("Data access or transaction failure while updating revenue in cash for ShiftReport ID: {}", shiftReportId, exception);
            throw exception;
        } catch (Exception exception) {
            log.error("An unexpected error occurred while updating revenue in cash for ShiftReport ID: {}", shiftReportId, exception);
            throw new PersistenceException(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
        }
    }

    /**
     * Updates the shift total for a ShiftReport record.
     *
     * @param shiftReportId  The ID of the ShiftReport record to update.
     * @param shiftTotal     The new shift total.
     * @param lastUpdatedBy  The user who last updated the record.
     * @param accessedBy     The user who accessed the record.
     * @throws TransactionException                  if a transaction error occurs during the operation.
     * @throws JDBCConnectionException               if there is a JDBC connection issue.
     * @throws JpaSystemException                    if there is a JPA system error.
     * @throws DataAccessResourceFailureException    if a data access resource fails.
     * @throws PersistenceException                  if a general persistence error occurs.
     */
    public void updateShiftTotal(UUID shiftReportId, String shiftTotal, String lastUpdatedBy, String accessedBy)
            throws TransactionException, JDBCConnectionException, JpaSystemException, DataAccessResourceFailureException {

        try {
            if (PersistenceStringUtils.isNotNullOrEmpty(shiftTotal)) {
                log.debug("Attempting to update shift total for ShiftReport ID: {}", shiftReportId);
                Date currentDate = new Date(TimeUtils.getFormattedCurrentPSTTime().getTime());
                Time currentTime = new Time(TimeUtils.getFormattedCurrentPSTTime().getTime());

                shiftReportRepository.updateShiftTotal(shiftReportId, shiftTotal, currentDate, currentTime, lastUpdatedBy, accessedBy);
                log.info("Shift total updated successfully for ShiftReport ID: {}", shiftReportId);
            } else {
                log.warn("Shift total is null or empty. Update aborted for ShiftReport ID: {}", shiftReportId);
            }
        } catch (DataAccessResourceFailureException | JDBCConnectionException | JpaSystemException
                 | TransactionException exception) {
            log.error("Data access or transaction failure while updating shift total for ShiftReport ID: {}", shiftReportId, exception);
            throw exception;
        } catch (Exception exception) {
            log.error("An unexpected error occurred while updating shift total for ShiftReport ID: {}", shiftReportId, exception);
            throw new PersistenceException(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
        }
    }

    /**
     * Updates the opening balance for a ShiftReport record.
     *
     * @param shiftReportId  The ID of the ShiftReport record to update.
     * @param openingBalance The new opening balance.
     * @param lastUpdatedBy  The user who last updated the record.
     * @param accessedBy     The user who accessed the record.
     * @throws TransactionException                  if a transaction error occurs during the operation.
     * @throws JDBCConnectionException               if there is a JDBC connection issue.
     * @throws JpaSystemException                    if there is a JPA system error.
     * @throws DataAccessResourceFailureException    if a data access resource fails.
     * @throws PersistenceException                  if a general persistence error occurs.
     */
    public void updateOpeningBalance(UUID shiftReportId, Float openingBalance, String lastUpdatedBy, String accessedBy)
            throws TransactionException, JDBCConnectionException, JpaSystemException, DataAccessResourceFailureException {

        try {
            log.debug("Attempting to update opening balance for ShiftReport ID: {}", shiftReportId);
            Date currentDate = new Date(TimeUtils.getFormattedCurrentPSTTime().getTime());
            Time currentTime = new Time(TimeUtils.getFormattedCurrentPSTTime().getTime());

            shiftReportRepository.updateOpeningBalance(shiftReportId, openingBalance, currentDate, currentTime, lastUpdatedBy, accessedBy);
            log.info("Opening balance updated successfully for ShiftReport ID: {}", shiftReportId);
        } catch (DataAccessResourceFailureException | JDBCConnectionException | JpaSystemException
                 | TransactionException exception) {
            log.error("Data access or transaction failure while updating opening balance for ShiftReport ID: {}", shiftReportId, exception);
            throw exception;
        } catch (Exception exception) {
            log.error("An unexpected error occurred while updating opening balance for ShiftReport ID: {}", shiftReportId, exception);
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
