package edu.csudh.lsu.persistence.repository.gamesroom.activity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * <p>
 * Activity Repository to manage Activity entity.
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
public interface Activity extends JpaRepository<Activity, UUID> {

    // Upsert an activity
    @Modifying
    @Transactional
    @Query(value = "UPSERT INTO ACTIVITY (ID, ACTIVITY, CATEGORY, PRICE, IMAGE_LOCATION) VALUES (:id, :activity, :category, :price, :imageLocation)", nativeQuery = true)
    void upsertActivity(@Param("id") UUID id, @Param("activity") String activity, @Param("category") String category, @Param("price") int price, @Param("imageLocation") String imageLocation);

    // List of categories
    @Query(value = "SELECT DISTINCT CATEGORY FROM ACTIVITY", nativeQuery = true)
    List<String> getAllCategories();
}
