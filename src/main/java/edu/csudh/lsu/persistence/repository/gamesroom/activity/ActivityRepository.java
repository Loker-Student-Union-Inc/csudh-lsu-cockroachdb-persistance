package edu.csudh.lsu.persistence.repository.gamesroom.activity;

import edu.csudh.lsu.persistence.model.activity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * ActivityRepository Repository to manage ActivityRepository entity.
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
public interface ActivityRepository extends JpaRepository<Activity, UUID> {

    // Upsert an activity
    @Modifying
    @Transactional
    @Query(value = "UPSERT INTO ACTIVITY (ID, ACTIVITY, CATEGORY, PRICE, IMAGE_LOCATION, CREATED_TIME, CREATED_DATE, LAST_UPDATED_BY, ACCESSED_BY) " +
            "VALUES (:id, :activity, :category, :price, :imageLocation, :createdTime, :createdDate, :lastUpdatedBy, :accessedBy)", nativeQuery = true)
    void upsertActivity(@Param("id") UUID id,
                        @Param("activity") String activity,
                        @Param("category") String category,
                        @Param("price") int price,
                        @Param("imageLocation") String imageLocation,
                        @Param("createdTime") Time createdTime,
                        @Param("createdDate") Date createdDate,
                        @Param("lastUpdatedBy") String lastUpdatedBy,
                        @Param("accessedBy") String accessedBy);


    // List of categories
    @Query(value = "SELECT DISTINCT CATEGORY FROM ACTIVITY", nativeQuery = true)
    List<String> getAllCategories();
}
