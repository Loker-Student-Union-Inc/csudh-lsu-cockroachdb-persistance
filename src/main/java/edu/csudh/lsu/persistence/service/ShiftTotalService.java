package edu.csudh.lsu.persistence.service;

import edu.csudh.lsu.persistence.constants.PersistenceConstants;
import edu.csudh.lsu.persistence.exception.PersistenceException;
import edu.csudh.lsu.persistence.model.shift.ShiftTotal;
import edu.csudh.lsu.persistence.repository.gamesroom.shift.ShiftTotalRepository;
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
import java.util.Objects;
import java.util.UUID;

/**
 * <p>
 * Shift Total Service class responsible for CRUD operation on Shift Total Table.
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
public class ShiftTotalService {

    @Autowired
    private ShiftTotalRepository shiftTotalRepository;

    /**
     * Saves or updates the provided ShiftTotal entity.
     *
     * @param shiftTotal The ShiftTotal entity to be saved or updated.
     * @throws TransactionException                  if a transaction error occurs during the operation.
     * @throws JDBCConnectionException               if there is a JDBC connection issue.
     * @throws JpaSystemException                    if there is a JPA system error.
     * @throws DataAccessResourceFailureException    if a data access resource fails.
     * @throws PersistenceException                  if a general persistence error occurs.
     */
    public void saveShiftTotal(ShiftTotal shiftTotal) throws TransactionException, JDBCConnectionException, JpaSystemException
            , DataAccessResourceFailureException {

        try {
            log.debug("Attempting to save shift total: {}", shiftTotal);
            if (Objects.nonNull(shiftTotal)) {
                shiftTotal.setCreatedTime(new Time(TimeUtils.getFormattedCurrentPSTTime().getTime()));
                shiftTotal.setCreatedDate(new Date(TimeUtils.getFormattedCurrentPSTTime().getTime()));
                shiftTotal.setLastUpdatedDate(new Date(TimeUtils.getFormattedCurrentPSTTime().getTime()));
                shiftTotal.setLastUpdatedTime(new Time(TimeUtils.getFormattedCurrentPSTTime().getTime()));

                shiftTotalRepository.upsertShiftTotal(
                        shiftTotal.getStudentName(),
                        shiftTotal.getAttendantName(),
                        shiftTotal.getActivity(),
                        shiftTotal.getCost(),
                        shiftTotal.getPaymentMode(),
                        shiftTotal.getStartTime(),
                        shiftTotal.getDate(),
                        shiftTotal.getDuration(),
                        shiftTotal.getCreatedTime(),
                        shiftTotal.getCreatedDate(),
                        shiftTotal.getLastUpdatedDate(),
                        shiftTotal.getLastUpdatedTime(),
                        shiftTotal.getLastUpdatedBy(),
                        shiftTotal.getAccessedBy()
                );
                log.info("Shift Total saved or updated successfully.");
            } else {
                log.warn("Attempted to save a null shift total.");
            }
        } catch (DataAccessResourceFailureException | JDBCConnectionException | JpaSystemException
                 | TransactionException exception) {
            log.error("Data access or transaction failure while saving shift total.", exception);
            throw exception;
        } catch (Exception exception) {
            log.error("An unexpected error occurred while saving shift total.", exception);
            throw new PersistenceException(PersistenceConstants.AN_EXCEPTION_OCCURRED_WHILE_UPSERTING_A_RECORD, exception.getMessage());
        }
    }

    /**
     * Updates the student name for a given ShiftTotal entity.
     *
     * @param id          The UUID of the ShiftTotal to update.
     * @param studentName The new student name.
     * @throws TransactionException                  if a transaction error occurs during the operation.
     * @throws JDBCConnectionException               if there is a JDBC connection issue.
     * @throws JpaSystemException                    if there is a JPA system error.
     * @throws DataAccessResourceFailureException    if a data access resource fails.
     * @throws PersistenceException                  if a general persistence error occurs.
     */
    public void updateStudentName(UUID id, String studentName) throws TransactionException, JDBCConnectionException, JpaSystemException
            , DataAccessResourceFailureException {

        try {
            log.debug("Attempting to update student name for Shift Total ID: {}", id);
            shiftTotalRepository.updateStudentName(id, studentName, new Date(TimeUtils.getFormattedCurrentPSTTime().getTime()), new Time(TimeUtils.getFormattedCurrentPSTTime().getTime()), "updatedByUser", "accessedByUser");
            log.info("Student name updated successfully for Shift Total ID: {}", id);
        } catch (DataAccessResourceFailureException | JDBCConnectionException | JpaSystemException
                 | TransactionException exception) {
            log.error("Data access or transaction failure while updating student name for Shift Total ID: {}", id, exception);
            throw exception;
        } catch (Exception exception) {
            log.error("An unexpected error occurred while updating student name for Shift Total ID: {}", id, exception);
            throw new PersistenceException(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
        }
    }

    // Similarly, add methods for updating other fields like updateAttendantName, updateActivity, etc.

    /**
     * Deletes a ShiftTotal entity by its ID.
     *
     * @param id The UUID of the ShiftTotal to delete.
     * @throws TransactionException                  if a transaction error occurs during the operation.
     * @throws JDBCConnectionException               if there is a JDBC connection issue.
     * @throws JpaSystemException                    if there is a JPA system error.
     * @throws DataAccessResourceFailureException    if a data access resource fails.
     * @throws PersistenceException                  if a general persistence error occurs.
     */
    public void deleteShiftTotal(UUID id) throws TransactionException, JDBCConnectionException, JpaSystemException
            , DataAccessResourceFailureException {

        try {
            log.debug("Attempting to delete Shift Total ID: {}", id);
            shiftTotalRepository.deleteShiftTotal(id);
            log.info("Shift Total ID: {} deleted successfully.", id);
        } catch (DataAccessResourceFailureException | JDBCConnectionException | JpaSystemException
                 | TransactionException exception) {
            log.error("Data access or transaction failure while deleting Shift Total ID: {}", id, exception);
            throw exception;
        } catch (Exception exception) {
            log.error("An unexpected error occurred while deleting Shift Total ID: {}", id, exception);
            throw new PersistenceException(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
        }
    }

    /**
     * Fetches all ShiftTotal records with pagination.
     *
     * @param pageable The pagination information.
     * @return A paginated list of ShiftTotal records.
     * @throws TransactionException                  if a transaction error occurs during the operation.
     * @throws JDBCConnectionException               if there is a JDBC connection issue.
     * @throws JpaSystemException                    if there is a JPA system error.
     * @throws DataAccessResourceFailureException    if a data access resource fails.
     * @throws PersistenceException                  if a general persistence error occurs.
     */
    public Page<ShiftTotal> findAllShiftTotals(Pageable pageable) throws TransactionException, JDBCConnectionException, JpaSystemException
            , DataAccessResourceFailureException {

        try {
            log.debug("Attempting to fetch all ShiftTotal records with pagination: {}", pageable);
            Page<ShiftTotal> shiftTotals = shiftTotalRepository.findAll(pageable);
            log.info("Fetched {} ShiftTotal records.", shiftTotals.getTotalElements());
            return shiftTotals;
        } catch (DataAccessResourceFailureException | JDBCConnectionException | JpaSystemException
                 | TransactionException exception) {
            log.error("Data access or transaction failure while fetching ShiftTotal records.", exception);
            throw exception;
        } catch (Exception exception) {
            log.error("An unexpected error occurred while fetching ShiftTotal records.", exception);
            throw new PersistenceException(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
        }
    }

    /**
     * Updates the student name for a ShiftTotal record.
     *
     * @param id             The ID of the ShiftTotal record to update.
     * @param studentName    The new student name.
     * @param lastUpdatedBy  The user who last updated the record.
     * @param accessedBy     The user who accessed the record.
     * @throws TransactionException                  if a transaction error occurs during the operation.
     * @throws JDBCConnectionException               if there is a JDBC connection issue.
     * @throws JpaSystemException                    if there is a JPA system error.
     * @throws DataAccessResourceFailureException    if a data access resource fails.
     * @throws PersistenceException                  if a general persistence error occurs.
     */
    public void updateStudentName(UUID id, String studentName, String lastUpdatedBy, String accessedBy) throws TransactionException, JDBCConnectionException, JpaSystemException
            , DataAccessResourceFailureException {

        try {
            log.debug("Attempting to update student name for ShiftTotal ID: {}", id);
            Date currentDate = new Date(TimeUtils.getFormattedCurrentPSTTime().getTime());
            Time currentTime = new Time(TimeUtils.getFormattedCurrentPSTTime().getTime());
            shiftTotalRepository.updateStudentName(id, studentName, currentDate, currentTime, lastUpdatedBy, accessedBy);
            log.info("Student name updated successfully for ShiftTotal ID: {}", id);
        } catch (DataAccessResourceFailureException | JDBCConnectionException | JpaSystemException
                 | TransactionException exception) {
            log.error("Data access or transaction failure while updating student name for ShiftTotal ID: {}", id, exception);
            throw exception;
        } catch (Exception exception) {
            log.error("An unexpected error occurred while updating student name for ShiftTotal ID: {}", id, exception);
            throw new PersistenceException(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
        }
    }

    /**
     * Updates the attendant name for a ShiftTotal record.
     *
     * @param id             The ID of the ShiftTotal record to update.
     * @param attendantName  The new attendant name.
     * @param lastUpdatedBy  The user who last updated the record.
     * @param accessedBy     The user who accessed the record.
     * @throws TransactionException                  if a transaction error occurs during the operation.
     * @throws JDBCConnectionException               if there is a JDBC connection issue.
     * @throws JpaSystemException                    if there is a JPA system error.
     * @throws DataAccessResourceFailureException    if a data access resource fails.
     * @throws PersistenceException                  if a general persistence error occurs.
     */
    public void updateAttendantName(UUID id, String attendantName, String lastUpdatedBy, String accessedBy) throws TransactionException, JDBCConnectionException, JpaSystemException
            , DataAccessResourceFailureException {

        try {
            log.debug("Attempting to update attendant name for ShiftTotal ID: {}", id);
            Date currentDate = new Date(TimeUtils.getFormattedCurrentPSTTime().getTime());
            Time currentTime = new Time(TimeUtils.getFormattedCurrentPSTTime().getTime());
            shiftTotalRepository.updateAttendantName(id, attendantName, currentDate, currentTime, lastUpdatedBy, accessedBy);
            log.info("Attendant name updated successfully for ShiftTotal ID: {}", id);
        } catch (DataAccessResourceFailureException | JDBCConnectionException | JpaSystemException
                 | TransactionException exception) {
            log.error("Data access or transaction failure while updating attendant name for ShiftTotal ID: {}", id, exception);
            throw exception;
        } catch (Exception exception) {
            log.error("An unexpected error occurred while updating attendant name for ShiftTotal ID: {}", id, exception);
            throw new PersistenceException(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
        }
    }

    /**
     * Updates the activity for a ShiftTotal record.
     *
     * @param id             The ID of the ShiftTotal record to update.
     * @param activity       The new activity.
     * @param lastUpdatedBy  The user who last updated the record.
     * @param accessedBy     The user who accessed the record.
     * @throws TransactionException                  if a transaction error occurs during the operation.
     * @throws JDBCConnectionException               if there is a JDBC connection issue.
     * @throws JpaSystemException                    if there is a JPA system error.
     * @throws DataAccessResourceFailureException    if a data access resource fails.
     * @throws PersistenceException                  if a general persistence error occurs.
     */
    public void updateActivity(UUID id, String activity, String lastUpdatedBy, String accessedBy) throws TransactionException, JDBCConnectionException, JpaSystemException
            , DataAccessResourceFailureException {

        try {
            log.debug("Attempting to update activity for ShiftTotal ID: {}", id);
            Date currentDate = new Date(TimeUtils.getFormattedCurrentPSTTime().getTime());
            Time currentTime = new Time(TimeUtils.getFormattedCurrentPSTTime().getTime());
            shiftTotalRepository.updateActivity(id, activity, currentDate, currentTime, lastUpdatedBy, accessedBy);
            log.info("Activity updated successfully for ShiftTotal ID: {}", id);
        } catch (DataAccessResourceFailureException | JDBCConnectionException | JpaSystemException
                 | TransactionException exception) {
            log.error("Data access or transaction failure while updating activity for ShiftTotal ID: {}", id, exception);
            throw exception;
        } catch (Exception exception) {
            log.error("An unexpected error occurred while updating activity for ShiftTotal ID: {}", id, exception);
            throw new PersistenceException(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
        }
    }

    /**
     * Updates the cost for a ShiftTotal record.
     *
     * @param id             The ID of the ShiftTotal record to update.
     * @param cost           The new cost.
     * @param lastUpdatedBy  The user who last updated the record.
     * @param accessedBy     The user who accessed the record.
     * @throws TransactionException                  if a transaction error occurs during the operation.
     * @throws JDBCConnectionException               if there is a JDBC connection issue.
     * @throws JpaSystemException                    if there is a JPA system error.
     * @throws DataAccessResourceFailureException    if a data access resource fails.
     * @throws PersistenceException                  if a general persistence error occurs.
     */
    public void updateCost(UUID id, Float cost, String lastUpdatedBy, String accessedBy) throws TransactionException, JDBCConnectionException, JpaSystemException
            , DataAccessResourceFailureException {

        try {
            log.debug("Attempting to update cost for ShiftTotal ID: {}", id);
            Date currentDate = new Date(TimeUtils.getFormattedCurrentPSTTime().getTime());
            Time currentTime = new Time(TimeUtils.getFormattedCurrentPSTTime().getTime());
            shiftTotalRepository.updateCost(id, cost, currentDate, currentTime, lastUpdatedBy, accessedBy);
            log.info("Cost updated successfully for ShiftTotal ID: {}", id);
        } catch (DataAccessResourceFailureException | JDBCConnectionException | JpaSystemException
                 | TransactionException exception) {
            log.error("Data access or transaction failure while updating cost for ShiftTotal ID: {}", id, exception);
            throw exception;
        } catch (Exception exception) {
            log.error("An unexpected error occurred while updating cost for ShiftTotal ID: {}", id, exception);
            throw new PersistenceException(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
        }
    }

    /**
     * Updates the payment mode for a ShiftTotal record.
     *
     * @param id             The ID of the ShiftTotal record to update.
     * @param paymentMode    The new payment mode.
     * @param lastUpdatedBy  The user who last updated the record.
     * @param accessedBy     The user who accessed the record.
     * @throws TransactionException                  if a transaction error occurs during the operation.
     * @throws JDBCConnectionException               if there is a JDBC connection issue.
     * @throws JpaSystemException                    if there is a JPA system error.
     * @throws DataAccessResourceFailureException    if a data access resource fails.
     * @throws PersistenceException                  if a general persistence error occurs.
     */
    public void updatePaymentMode(UUID id, String paymentMode, String lastUpdatedBy, String accessedBy) throws TransactionException, JDBCConnectionException, JpaSystemException
            , DataAccessResourceFailureException {

        try {
            log.debug("Attempting to update payment mode for ShiftTotal ID: {}", id);
            Date currentDate = new Date(TimeUtils.getFormattedCurrentPSTTime().getTime());
            Time currentTime = new Time(TimeUtils.getFormattedCurrentPSTTime().getTime());
            shiftTotalRepository.updatePaymentMode(id, paymentMode, currentDate, currentTime, lastUpdatedBy, accessedBy);
            log.info("Payment mode updated successfully for ShiftTotal ID: {}", id);
        } catch (DataAccessResourceFailureException | JDBCConnectionException | JpaSystemException
                 | TransactionException exception) {
            log.error("Data access or transaction failure while updating payment mode for ShiftTotal ID: {}", id, exception);
            throw exception;
        } catch (Exception exception) {
            log.error("An unexpected error occurred while updating payment mode for ShiftTotal ID: {}", id, exception);
            throw new PersistenceException(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
        }
    }

    /**
     * Updates the duration for a ShiftTotal record.
     *
     * @param id             The ID of the ShiftTotal record to update.
     * @param duration       The new duration.
     * @param lastUpdatedBy  The user who last updated the record.
     * @param accessedBy     The user who accessed the record.
     * @throws TransactionException                  if a transaction error occurs during the operation.
     * @throws JDBCConnectionException               if there is a JDBC connection issue.
     * @throws JpaSystemException                    if there is a JPA system error.
     * @throws DataAccessResourceFailureException    if a data access resource fails.
     * @throws PersistenceException                  if a general persistence error occurs.
     */
    public void updateDuration(UUID id, String duration, String lastUpdatedBy, String accessedBy) throws TransactionException, JDBCConnectionException, JpaSystemException
            , DataAccessResourceFailureException {

        try {
            log.debug("Attempting to update duration for ShiftTotal ID: {}", id);
            Date currentDate = new Date(TimeUtils.getFormattedCurrentPSTTime().getTime());
            Time currentTime = new Time(TimeUtils.getFormattedCurrentPSTTime().getTime());
            shiftTotalRepository.updateDuration(id, duration, currentDate, currentTime, lastUpdatedBy, accessedBy);
            log.info("Duration updated successfully for ShiftTotal ID: {}", id);
        } catch (DataAccessResourceFailureException | JDBCConnectionException | JpaSystemException
                 | TransactionException exception) {
            log.error("Data access or transaction failure while updating duration for ShiftTotal ID: {}", id, exception);
            throw exception;
        } catch (Exception exception) {
            log.error("An unexpected error occurred while updating duration for ShiftTotal ID: {}", id, exception);
            throw new PersistenceException(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
        }
    }


    /**
     * Calculates the total costs for a given attendant for a specific date.
     *
     * @param attendantName The name of the attendant.
     * @param date          The date for which to calculate the total costs.
     * @return An object containing the total costs.
     * @throws TransactionException                  if a transaction error occurs during the operation.
     * @throws JDBCConnectionException               if there is a JDBC connection issue.
     * @throws JpaSystemException                    if there is a JPA system error.
     * @throws DataAccessResourceFailureException    if a data access resource fails.
     * @throws PersistenceException                  if a general persistence error occurs.
     */
    public Object calculateTotalCostsByAttendantNameAndDate(String attendantName, Date date) throws TransactionException, JDBCConnectionException, JpaSystemException
            , DataAccessResourceFailureException {

        try {
            log.debug("Attempting to calculate total costs for attendant '{}' on date '{}'", attendantName, date);
            Object totalCosts = shiftTotalRepository.findTotalCostsByAttendantNameAndDate(attendantName, date);
            log.info("Calculated total costs for attendant '{}'.", attendantName);
            return totalCosts;
        } catch (DataAccessResourceFailureException | JDBCConnectionException | JpaSystemException
                 | TransactionException exception) {
            log.error("Data access or transaction failure while calculating total costs for attendant '{}' on date '{}'", attendantName, date, exception);
            throw exception;
        } catch (Exception exception) {
            log.error("An unexpected error occurred while calculating total costs for attendant '{}' on date '{}'", attendantName, date, exception);
            throw new PersistenceException(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
        }
    }
}
