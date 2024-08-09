package edu.csudh.lsu.persistence.service;

import edu.csudh.lsu.persistence.constants.PersistenceConstants;
import edu.csudh.lsu.persistence.exception.PersistenceException;
import edu.csudh.lsu.persistence.model.profile.Profile;
import edu.csudh.lsu.persistence.repository.gamesroom.profile.ProfileRepository;
import edu.csudh.lsu.persistence.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.TransactionException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * Profile Service class responsible for CRUD operation on Profile Table.
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
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    /**
     * Saves or updates the provided Profile entity.
     *
     * @param profile The Profile entity to be saved or updated.
     * @throws TransactionException                  if a transaction error occurs during the operation.
     * @throws JDBCConnectionException               if there is a JDBC connection issue.
     * @throws JpaSystemException                    if there is a JPA system error.
     * @throws DataAccessResourceFailureException    if a data access resource fails.
     * @throws PersistenceException                  if a general persistence error occurs.
     */
    public void saveOrUpdateProfile(Profile profile) throws TransactionException, JDBCConnectionException, JpaSystemException
            , DataAccessResourceFailureException {

        try {
            log.debug("Attempting to save or update profile: {}", profile);

            if (Objects.nonNull(profile)) {
                // If it's a new profile (no existing ID), set the created date and time
                if (profile.getUserId() == null) {
                    profile.setCreatedDate(new Date(TimeUtils.getFormattedCurrentPSTTime().getTime()));
                    profile.setCreatedTime(new Time(TimeUtils.getFormattedCurrentPSTTime().getTime()));
                    log.debug("Setting createdDate and createdTime for a new profile.");
                }

                // Set last updated date and time to current PST time
                profile.setLastUpdatedDate(new Date(TimeUtils.getFormattedCurrentPSTTime().getTime()));
                profile.setLastUpdatedTime(new Time(TimeUtils.getFormattedCurrentPSTTime().getTime()));

                profileRepository.upsertProfile(profile.getUserId(), profile.getUserPassword(), profile.getFirstName(),
                        profile.getLastName(), profile.getRole(), profile.getPermission(), profile.getCreatedTime(),
                        profile.getCreatedDate(), profile.getLastUpdatedBy(), profile.getAccessedBy());
                log.info("Profile saved or updated successfully with User ID: {}", profile.getUserId());
            } else {
                log.warn("Attempted to save or update a null profile.");
            }
        } catch (DataAccessResourceFailureException | JDBCConnectionException | JpaSystemException
                 | TransactionException exception) {
            log.error("Data access or transaction failure while saving or updating profile with User ID: {}", profile.getUserId(), exception);
            throw exception;
        } catch (Exception exception) {
            log.error("An unexpected error occurred while saving or updating profile with User ID: {}", profile.getUserId(), exception);
            throw new PersistenceException(PersistenceConstants.AN_EXCEPTION_OCCURRED_WHILE_UPSERTING_A_RECORD, exception.getMessage());
        }
    }

    /**
     * Deletes the Profile entity with the specified User ID.
     *
     * @param userId The User ID of the Profile to be deleted.
     * @throws TransactionException                  if a transaction error occurs during the operation.
     * @throws JDBCConnectionException               if there is a JDBC connection issue.
     * @throws JpaSystemException                    if there is a JPA system error.
     * @throws DataAccessResourceFailureException    if a data access resource fails.
     * @throws PersistenceException                  if a general persistence error occurs.
     */
    public void deleteProfile(String userId) throws TransactionException, JDBCConnectionException, JpaSystemException
            , DataAccessResourceFailureException {

        try {
            log.debug("Attempting to delete profile with User ID: {}", userId);
            profileRepository.deleteProfile(userId);
            log.info("Profile deleted successfully with User ID: {}", userId);
        } catch (DataAccessResourceFailureException | JDBCConnectionException | JpaSystemException
                 | TransactionException exception) {
            log.error("Data access or transaction failure while deleting profile with User ID: {}", userId, exception);
            throw exception;
        } catch (Exception exception) {
            log.error("An unexpected error occurred while deleting profile with User ID: {}", userId, exception);
            throw new PersistenceException(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
        }
    }

    /**
     * Fetches all profiles from the repository.
     *
     * @return A list of all profiles.
     * @throws TransactionException                  if a transaction error occurs during the operation.
     * @throws JDBCConnectionException               if there is a JDBC connection issue.
     * @throws JpaSystemException                    if there is a JPA system error.
     * @throws DataAccessResourceFailureException    if a data access resource fails.
     * @throws PersistenceException                  if a general persistence error occurs.
     */
    public List<Profile> findAllProfiles() throws TransactionException, JDBCConnectionException, JpaSystemException
            , DataAccessResourceFailureException {

        try {
            log.debug("Attempting to fetch all profiles");
            List<Profile> profiles = profileRepository.findAllProfiles();
            log.info("Fetched {} profiles.", profiles.size());
            return profiles;
        } catch (DataAccessResourceFailureException | JDBCConnectionException | JpaSystemException
                 | TransactionException exception) {
            log.error("Data access or transaction failure while fetching profiles.", exception);
            throw exception;
        } catch (Exception exception) {
            log.error("An unexpected error occurred while fetching profiles.", exception);
            throw new PersistenceException(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
        }
    }

    /**
     * Updates the password for the Profile entity with the specified User ID.
     *
     * @param userId      The User ID of the Profile to update.
     * @param userPassword The new password.
     * @throws TransactionException                  if a transaction error occurs during the operation.
     * @throws JDBCConnectionException               if there is a JDBC connection issue.
     * @throws JpaSystemException                    if there is a JPA system error.
     * @throws DataAccessResourceFailureException    if a data access resource fails.
     * @throws PersistenceException                  if a general persistence error occurs.
     */
    public void updatePassword(String userId, String userPassword) throws TransactionException, JDBCConnectionException, JpaSystemException
            , DataAccessResourceFailureException {

        try {
            log.debug("Attempting to update password for User ID: {}", userId);
            // Update the last updated date and time to current PST time
            Date lastUpdatedDate = new Date(TimeUtils.getFormattedCurrentPSTTime().getTime());
            Time lastUpdatedTime = new Time(TimeUtils.getFormattedCurrentPSTTime().getTime());

            profileRepository.updatePassword(userId, userPassword, "system", lastUpdatedDate, lastUpdatedTime, "system");
            log.info("Password updated successfully for User ID: {}", userId);
        } catch (DataAccessResourceFailureException | JDBCConnectionException | JpaSystemException
                 | TransactionException exception) {
            log.error("Data access or transaction failure while updating password for User ID: {}", userId, exception);
            throw exception;
        } catch (Exception exception) {
            log.error("An unexpected error occurred while updating password for User ID: {}", userId, exception);
            throw new PersistenceException(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
        }
    }

    /**
     * Updates the first name for the Profile entity with the specified User ID.
     *
     * @param userId      The User ID of the Profile to update.
     * @param firstName   The new first name.
     * @throws TransactionException                  if a transaction error occurs during the operation.
     * @throws JDBCConnectionException               if there is a JDBC connection issue.
     * @throws JpaSystemException                    if there is a JPA system error.
     * @throws DataAccessResourceFailureException    if a data access resource fails.
     * @throws PersistenceException                  if a general persistence error occurs.
     */
    public void updateFirstName(String userId, String firstName) throws TransactionException, JDBCConnectionException, JpaSystemException
            , DataAccessResourceFailureException {

        try {
            log.debug("Attempting to update first name for User ID: {}", userId);
            // Update the last updated date and time to current PST time
            Date lastUpdatedDate = new Date(TimeUtils.getFormattedCurrentPSTTime().getTime());
            Time lastUpdatedTime = new Time(TimeUtils.getFormattedCurrentPSTTime().getTime());

            profileRepository.updateFirstName(userId, firstName, "system", lastUpdatedDate, lastUpdatedTime, "system");
            log.info("First name updated successfully for User ID: {}", userId);
        } catch (DataAccessResourceFailureException | JDBCConnectionException | JpaSystemException
                 | TransactionException exception) {
            log.error("Data access or transaction failure while updating first name for User ID: {}", userId, exception);
            throw exception;
        } catch (Exception exception) {
            log.error("An unexpected error occurred while updating first name for User ID: {}", userId, exception);
            throw new PersistenceException(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
        }
    }

    /**
     * Updates the last name for the Profile entity with the specified User ID.
     *
     * @param userId      The User ID of the Profile to update.
     * @param lastName    The new last name.
     * @throws TransactionException                  if a transaction error occurs during the operation.
     * @throws JDBCConnectionException               if there is a JDBC connection issue.
     * @throws JpaSystemException                    if there is a JPA system error.
     * @throws DataAccessResourceFailureException    if a data access resource fails.
     * @throws PersistenceException                  if a general persistence error occurs.
     */
    public void updateLastName(String userId, String lastName) throws TransactionException, JDBCConnectionException, JpaSystemException
            , DataAccessResourceFailureException {

        try {
            log.debug("Attempting to update last name for User ID: {}", userId);
            // Update the last updated date and time to current PST time
            Date lastUpdatedDate = new Date(TimeUtils.getFormattedCurrentPSTTime().getTime());
            Time lastUpdatedTime = new Time(TimeUtils.getFormattedCurrentPSTTime().getTime());

            profileRepository.updateLastName(userId, lastName, "system", lastUpdatedDate, lastUpdatedTime, "system");
            log.info("Last name updated successfully for User ID: {}", userId);
        } catch (DataAccessResourceFailureException | JDBCConnectionException | JpaSystemException
                 | TransactionException exception) {
            log.error("Data access or transaction failure while updating last name for User ID: {}", userId, exception);
            throw exception;
        } catch (Exception exception) {
            log.error("An unexpected error occurred while updating last name for User ID: {}", userId, exception);
            throw new PersistenceException(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
        }
    }

    /**
     * Updates the role for the Profile entity with the specified User ID.
     *
     * @param userId      The User ID of the Profile to update.
     * @param role        The new role.
     * @throws TransactionException                  if a transaction error occurs during the operation.
     * @throws JDBCConnectionException               if there is a JDBC connection issue.
     * @throws JpaSystemException                    if there is a JPA system error.
     * @throws DataAccessResourceFailureException    if a data access resource fails.
     * @throws PersistenceException                  if a general persistence error occurs.
     */
    public void updateRole(String userId, String role) throws TransactionException, JDBCConnectionException, JpaSystemException
            , DataAccessResourceFailureException {

        try {
            log.debug("Attempting to update role for User ID: {}", userId);
            // Update the last updated date and time to current PST time
            Date lastUpdatedDate = new Date(TimeUtils.getFormattedCurrentPSTTime().getTime());
            Time lastUpdatedTime = new Time(TimeUtils.getFormattedCurrentPSTTime().getTime());

            profileRepository.updateRole(userId, role, "system", lastUpdatedDate, lastUpdatedTime, "system");
            log.info("Role updated successfully for User ID: {}", userId);
        } catch (DataAccessResourceFailureException | JDBCConnectionException | JpaSystemException
                 | TransactionException exception) {
            log.error("Data access or transaction failure while updating role for User ID: {}", userId, exception);
            throw exception;
        } catch (Exception exception) {
            log.error("An unexpected error occurred while updating role for User ID: {}", userId, exception);
            throw new PersistenceException(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
        }
    }

    /**
     * Updates the permission for the Profile entity with the specified User ID.
     *
     * @param userId      The User ID of the Profile to update.
     * @param permission  The new permission.
     * @throws TransactionException                  if a transaction error occurs during the operation.
     * @throws JDBCConnectionException               if there is a JDBC connection issue.
     * @throws JpaSystemException                    if there is a JPA system error.
     * @throws DataAccessResourceFailureException    if a data access resource fails.
     * @throws PersistenceException                  if a general persistence error occurs.
     */
    public void updatePermission(String userId, String permission) throws TransactionException, JDBCConnectionException, JpaSystemException
            , DataAccessResourceFailureException {

        try {
            log.debug("Attempting to update permission for User ID: {}", userId);
            // Update the last updated date and time to current PST time
            Date lastUpdatedDate = new Date(TimeUtils.getFormattedCurrentPSTTime().getTime());
            Time lastUpdatedTime = new Time(TimeUtils.getFormattedCurrentPSTTime().getTime());

            profileRepository.updatePermission(userId, permission, "system", lastUpdatedDate, lastUpdatedTime, "system");
            log.info("Permission updated successfully for User ID: {}", userId);
        } catch (DataAccessResourceFailureException | JDBCConnectionException | JpaSystemException
                 | TransactionException exception) {
            log.error("Data access or transaction failure while updating permission for User ID: {}", userId, exception);
            throw exception;
        } catch (Exception exception) {
            log.error("An unexpected error occurred while updating permission for User ID: {}", userId, exception);
            throw new PersistenceException(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
        }
    }
}
