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
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

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
    public void saveActivity(Activity activity) throws TransactionException, JDBCConnectionException, JpaSystemException
            , DataAccessResourceFailureException {

        try {
            log.debug("Attempting to save activity: {}", activity);
            if (Objects.nonNull(activity)) {
                activityRepository.upsertActivity(activity.getActivity(), activity.getCategory(),
                        activity.getPrice(), activity.getImageLocation(),
                        activity.getCreatedTime(), activity.getCreatedDate(),
                        activity.getLastUpdatedBy(), activity.getAccessedBy());
                log.info("Activity '{}' saved or updated successfully.", activity.getActivity());
            } else {
                log.warn("Attempted to save a null activity.");
            }
        } catch (DataAccessResourceFailureException | JDBCConnectionException | JpaSystemException
                 | TransactionException exception) {
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
}
