package edu.csudh.lsu.persistence.service;

import edu.csudh.lsu.persistence.constants.PersistenceConstants;
import edu.csudh.lsu.persistence.exception.PersistenceException;
import edu.csudh.lsu.persistence.model.activity.Activity;
import edu.csudh.lsu.persistence.repository.gamesroom.activity.ActivityRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.TransactionException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * <p>
 * Activity Service class responsible for CRUD operation on Activity Table.
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
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    /**
     * Saves the provided Activity entity. If the entity already exists, it is updated.
     *
     * @param activity The Activity entity to be saved or updated.
     * @throws TransactionException                  if a transaction error occurs during the operation.
     * @throws JDBCConnectionException               if there is a JDBC connection issue.
     * @throws JpaSystemException                    if there is a JPA system error.
     * @throws DataAccessResourceFailureException    if a data access resource fails.
     * @throws PersistenceException                  if a general persistence error occurs.
     */
    @Modifying
    @Transactional
    public void saveActivity(Activity activity) throws TransactionException, JDBCConnectionException, JpaSystemException, DataAccessResourceFailureException {

        try {
            log.debug("Attempting to save activity: {}", activity);
            if (Objects.nonNull(activity)) {
                // Set createdDate and createdTime if the activity is new (no existing ID)

                var now = LocalDateTime.now(ZoneId.of("America/Los_Angeles"));
                activity.setCreatedDate(Date.valueOf(now.toLocalDate()));
                activity.setCreatedTime(Time.valueOf(now.toLocalTime()));
                log.debug("Setting createdDate and createdTime for new activity.");


                // Save the activity entity
                activityRepository.save(activity);
                log.info("Activity '{}' saved or updated successfully.", activity.getActivity());
            } else {
                log.warn("Attempted to save a null activity.");
            }
        } catch (DataAccessResourceFailureException | JDBCConnectionException | JpaSystemException | TransactionException exception) {
            log.error("Data access or transaction failure while saving activity '{}'.", activity != null ? activity.getActivity() : "null", exception);
            throw exception;
        } catch (Exception exception) {
            log.error("An unexpected error occurred while saving activity '{}'.", activity != null ? activity.getActivity() : "null", exception);
            throw new PersistenceException(PersistenceConstants.AN_EXCEPTION_OCCURRED_WHILE_UPSERTING_A_RECORD, exception.getMessage());
        }
    }

    /**
     * Fetches all unique activity categories from the repository.
     *
     * @return categoryList List<String>             A list of all unique categories.
     * @throws TransactionException                  if a transaction error occurs during the operation.
     * @throws JDBCConnectionException               if there is a JDBC connection issue.
     * @throws JpaSystemException                    if there is a JPA system error.
     * @throws DataAccessResourceFailureException    if a data access resource fails.
     * @throws PersistenceException                  if a general persistence error occurs.
     */
    public List<String> fetchAllCategories() throws TransactionException, JDBCConnectionException, JpaSystemException
            , DataAccessResourceFailureException {

        try {
            var categoryList = activityRepository.getAllCategories();
            log.info("Fetched {} categories. Categories: {}", CollectionUtils.isEmpty(categoryList) ? "no" : categoryList.size(), categoryList);
            return categoryList;
        } catch (DataAccessResourceFailureException | JDBCConnectionException | JpaSystemException
                 | TransactionException exception) {
            log.error("Data access or transaction failure while fetching categories.", exception);
            throw exception;
        } catch (Exception exception) {
            log.error("An unexpected error occurred while fetching categories.", exception);
            throw new PersistenceException(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
        }
    }

    /**
     * Deletes the activity with the given ID.
     *
     * @param id The UUID of the activity to be deleted.
     * @throws TransactionException                  if a transaction error occurs during the operation.
     * @throws JDBCConnectionException               if there is a JDBC connection issue.
     * @throws JpaSystemException                    if there is a JPA system error.
     * @throws DataAccessResourceFailureException    if a data access resource fails.
     * @throws PersistenceException                  if a general persistence error occurs.
     */
    @Modifying
    @Transactional
    public void deleteActivity(UUID id) throws TransactionException, JDBCConnectionException, JpaSystemException, DataAccessResourceFailureException {
        try {
            log.debug("Attempting to delete activity with ID: {}", id);
            activityRepository.deleteActivityById(id);
            log.info("Activity with ID '{}' deleted successfully.", id);
        } catch (DataAccessResourceFailureException | JDBCConnectionException | JpaSystemException | TransactionException exception) {
            log.error("Data access or transaction failure while deleting activity with ID '{}'.", id, exception);
            throw exception;
        } catch (Exception exception) {
            log.error("An unexpected error occurred while deleting activity with ID '{}'.", id, exception);
            throw new PersistenceException(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
        }
    }

    /**
     * Updates the provided Activity entity.
     *
     * @param activity The Activity entity to be updated.
     * @throws TransactionException                  if a transaction error occurs during the operation.
     * @throws JDBCConnectionException               if there is a JDBC connection issue.
     * @throws JpaSystemException                    if there is a JPA system error.
     * @throws DataAccessResourceFailureException    if a data access resource fails.
     * @throws PersistenceException                  if a general persistence error occurs.
     */
    @Modifying
    @Transactional
    public void updateActivity(Activity activity) throws TransactionException, JDBCConnectionException, JpaSystemException, DataAccessResourceFailureException {
        try {
            log.debug("Attempting to update activity: {}", activity);
            if (Objects.nonNull(activity) && activity.getId() != null) {
                var now = LocalDateTime.now(ZoneId.of("America/Los_Angeles"));
                activity.setLastUpdatedBy("User"); // Set this to the appropriate user
                activity.setCreatedDate(Date.valueOf(now.toLocalDate())); // Optionally set createdDate or use another field to track the update
                activityRepository.save(activity);
                log.info("Activity '{}' updated successfully.", activity.getActivity());
            } else {
                log.warn("Attempted to update a null activity or activity with a null ID.");
            }
        } catch (DataAccessResourceFailureException | JDBCConnectionException | JpaSystemException | TransactionException exception) {
            log.error("Data access or transaction failure while updating activity '{}'.", activity != null ? activity.getActivity() : "null", exception);
            throw exception;
        } catch (Exception exception) {
            log.error("An unexpected error occurred while updating activity '{}'.", activity != null ? activity.getActivity() : "null", exception);
            throw new PersistenceException(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
        }
    }
}
