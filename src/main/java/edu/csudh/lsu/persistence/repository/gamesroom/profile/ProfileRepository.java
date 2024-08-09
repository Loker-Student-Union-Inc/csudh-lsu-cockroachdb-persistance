package edu.csudh.lsu.persistence.repository.gamesroom.profile;

import edu.csudh.lsu.persistence.model.profile.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

/**
 * <p>
 * Interface used as Repository for managing Profile entity.
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

@Repository
public interface ProfileRepository extends JpaRepository<Profile, String> {

    // Upsert a profile
    @Modifying
    @Transactional
    @Query(value = "UPSERT INTO PROFILE (USER_ID, USER_PASSWORD, FIRST_NAME, LAST_NAME, ROLE, PERMISSION, CREATED_TIME, CREATED_DATE, LAST_UPDATED_BY, ACCESSED_BY) " +
            "VALUES (:userId, :userPassword, :firstName, :lastName, :role, :permission, :createdTime, :createdDate, :lastUpdatedBy, :accessedBy)", nativeQuery = true)
    void upsertProfile(@Param("userId") String userId,
                       @Param("userPassword") String userPassword,
                       @Param("firstName") String firstName,
                       @Param("lastName") String lastName,
                       @Param("role") String role,
                       @Param("permission") String permission,
                       @Param("createdTime") Time createdTime,
                       @Param("createdDate") Date createdDate,
                       @Param("lastUpdatedBy") String lastUpdatedBy,
                       @Param("accessedBy") String accessedBy);

    // Delete a profile (considering lastUpdatedDate and lastUpdatedTime)
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM PROFILE WHERE USER_ID = :userId", nativeQuery = true)
    void deleteProfile(@Param("userId") String userId);

    // List all profiles
    @Query(value = "SELECT * FROM PROFILE", nativeQuery = true)
    List<Profile> findAllProfiles();

    // Update password
    @Modifying
    @Transactional
    @Query(value = "UPDATE PROFILE SET USER_PASSWORD = :userPassword, LAST_UPDATED_BY = :lastUpdatedBy, LAST_UPDATED_DATE = :lastUpdatedDate, LAST_UPDATED_TIME = :lastUpdatedTime, ACCESSED_BY = :accessedBy WHERE USER_ID = :userId", nativeQuery = true)
    void updatePassword(@Param("userId") String userId,
                        @Param("userPassword") String userPassword,
                        @Param("lastUpdatedBy") String lastUpdatedBy,
                        @Param("lastUpdatedDate") Date lastUpdatedDate,
                        @Param("lastUpdatedTime") Time lastUpdatedTime,
                        @Param("accessedBy") String accessedBy);

    // Update first name (considering lastUpdatedDate and lastUpdatedTime)
    @Modifying
    @Transactional
    @Query(value = "UPDATE PROFILE SET FIRST_NAME = :firstName, LAST_UPDATED_BY = :lastUpdatedBy, LAST_UPDATED_DATE = :lastUpdatedDate, LAST_UPDATED_TIME = :lastUpdatedTime, ACCESSED_BY = :accessedBy WHERE USER_ID = :userId", nativeQuery = true)
    void updateFirstName(@Param("userId") String userId,
                         @Param("firstName") String firstName,
                         @Param("lastUpdatedBy") String lastUpdatedBy,
                         @Param("lastUpdatedDate") Date lastUpdatedDate,
                         @Param("lastUpdatedTime") Time lastUpdatedTime,
                         @Param("accessedBy") String accessedBy);

    // Update last name (considering lastUpdatedDate and lastUpdatedTime)
    @Modifying
    @Transactional
    @Query(value = "UPDATE PROFILE SET LAST_NAME = :lastName, LAST_UPDATED_BY = :lastUpdatedBy, LAST_UPDATED_DATE = :lastUpdatedDate, LAST_UPDATED_TIME = :lastUpdatedTime, ACCESSED_BY = :accessedBy WHERE USER_ID = :userId", nativeQuery = true)
    void updateLastName(@Param("userId") String userId,
                        @Param("lastName") String lastName,
                        @Param("lastUpdatedBy") String lastUpdatedBy,
                        @Param("lastUpdatedDate") Date lastUpdatedDate,
                        @Param("lastUpdatedTime") Time lastUpdatedTime,
                        @Param("accessedBy") String accessedBy);

    // Update role
    @Modifying
    @Transactional
    @Query(value = "UPDATE PROFILE SET ROLE = :role, LAST_UPDATED_BY = :lastUpdatedBy, LAST_UPDATED_DATE = :lastUpdatedDate, LAST_UPDATED_TIME = :lastUpdatedTime, ACCESSED_BY = :accessedBy WHERE USER_ID = :userId", nativeQuery = true)
    void updateRole(@Param("userId") String userId,
                    @Param("role") String role,
                    @Param("lastUpdatedBy") String lastUpdatedBy,
                    @Param("lastUpdatedDate") Date lastUpdatedDate,
                    @Param("lastUpdatedTime") Time lastUpdatedTime,
                    @Param("accessedBy") String accessedBy);

    // Update permission
    @Modifying
    @Transactional
    @Query(value = "UPDATE PROFILE SET PERMISSION = :permission, LAST_UPDATED_BY = :lastUpdatedBy, LAST_UPDATED_DATE = :lastUpdatedDate, LAST_UPDATED_TIME = :lastUpdatedTime, ACCESSED_BY = :accessedBy WHERE USER_ID = :userId", nativeQuery = true)
    void updatePermission(@Param("userId") String userId,
                          @Param("permission") String permission,
                          @Param("lastUpdatedBy") String lastUpdatedBy,
                          @Param("lastUpdatedDate") Date lastUpdatedDate,
                          @Param("lastUpdatedTime") Time lastUpdatedTime,
                          @Param("accessedBy") String accessedBy);

}
