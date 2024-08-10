package edu.csudh.lsu.persistence.service;

import edu.csudh.lsu.persistence.constants.PersistenceConstants;
import edu.csudh.lsu.persistence.exception.PersistenceException;
import edu.csudh.lsu.persistence.model.activity.Activity;
import edu.csudh.lsu.persistence.repository.gamesroom.activity.ActivityRepository;
import org.hibernate.TransactionException;
import org.hibernate.exception.JDBCConnectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.jpa.JpaSystemException;

import java.sql.Date;
import java.sql.Time;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ActivityServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    @InjectMocks
    private ActivityService activityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveActivity_whenActivityIsNull_logsWarning() {
        // Act
        activityService.saveActivity(null);

        // Assert
        verify(activityRepository, never()).upsertActivity(anyString(), anyString(), anyInt(), anyString(), any(), any(), anyString(), anyString());
    }

    @Test
    void saveActivity_whenValidActivity_savesSuccessfully() {
        // Arrange
        Activity activity = createSampleActivity();

        // Act
        activityService.saveActivity(activity);

        // Assert
        verify(activityRepository, times(1)).upsertActivity(
                eq(activity.getActivity()), eq(activity.getCategory()), eq(activity.getPrice()),
                eq(activity.getImageLocation()), eq(activity.getCreatedTime()), eq(activity.getCreatedDate()),
                eq(activity.getLastUpdatedBy()), eq(activity.getAccessedBy())
        );
    }

    @Test
    void saveActivity_whenDataAccessResourceFailureException_throwsException() {
        // Arrange
        Activity activity = createSampleActivity();
        doThrow(new DataAccessResourceFailureException("Data access failure"))
                .when(activityRepository).upsertActivity(anyString(), anyString(), anyInt(), anyString(), any(), any(), anyString(), anyString());

        // Act & Assert
        assertThrows(DataAccessResourceFailureException.class, () -> activityService.saveActivity(activity));
    }

    @Test
    void saveActivity_whenJDBCConnectionException_throwsException() {
        // Arrange
        Activity activity = createSampleActivity();
        doThrow(new JDBCConnectionException("JDBC connection failure", null))
                .when(activityRepository).upsertActivity(anyString(), anyString(), anyInt(), anyString(), any(), any(), anyString(), anyString());

        // Act & Assert
        assertThrows(JDBCConnectionException.class, () -> activityService.saveActivity(activity));
    }

    @Test
    void saveActivity_whenJpaSystemException_throwsException() {
        // Arrange
        Activity activity = createSampleActivity();
        doThrow(new JpaSystemException(new RuntimeException("JPA system failure")))
                .when(activityRepository).upsertActivity(anyString(), anyString(), anyInt(), anyString(), any(), any(), anyString(), anyString());

        // Act & Assert
        assertThrows(JpaSystemException.class, () -> activityService.saveActivity(activity));
    }

    @Test
    void saveActivity_whenTransactionException_throwsException() {
        // Arrange
        Activity activity = createSampleActivity();
        doThrow(new TransactionException("Transaction failure"))
                .when(activityRepository).upsertActivity(anyString(), anyString(), anyInt(), anyString(), any(), any(), anyString(), anyString());

        // Act & Assert
        assertThrows(TransactionException.class, () -> activityService.saveActivity(activity));
    }

    @Test
    void saveActivity_whenUnexpectedException_throwsPersistenceException() {
        // Arrange
        Activity activity = createSampleActivity();
        doThrow(new RuntimeException("Unexpected error"))
                .when(activityRepository).upsertActivity(anyString(), anyString(), anyInt(), anyString(), any(), any(), anyString(), anyString());

        // Act & Assert
        PersistenceException exception = assertThrows(PersistenceException.class, () -> activityService.saveActivity(activity));
        assertEquals(PersistenceConstants.AN_EXCEPTION_OCCURRED_WHILE_UPSERTING_A_RECORD, exception.getMessage());
    }

    @Test
    void fetchAllCategories_whenValidData_returnsCategories() {
        // Arrange
        List<String> categories = Arrays.asList("Category1", "Category2");
        when(activityRepository.getAllCategories()).thenReturn(categories);

        // Act
        List<String> result = activityService.fetchAllCategories();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Category1", result.get(0));
        assertEquals("Category2", result.get(1));
    }

    @Test
    void fetchAllCategories_whenNoData_returnsEmptyList() {
        // Arrange
        when(activityRepository.getAllCategories()).thenReturn(Collections.emptyList());

        // Act
        List<String> result = activityService.fetchAllCategories();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void fetchAllCategories_whenDataAccessResourceFailureException_throwsException() {
        // Arrange
        doThrow(new DataAccessResourceFailureException("Data access failure"))
                .when(activityRepository).getAllCategories();

        // Act & Assert
        assertThrows(DataAccessResourceFailureException.class, () -> activityService.fetchAllCategories());
    }

    @Test
    void fetchAllCategories_whenJDBCConnectionException_throwsException() {
        // Arrange
        doThrow(new JDBCConnectionException("JDBC connection failure", null))
                .when(activityRepository).getAllCategories();

        // Act & Assert
        assertThrows(JDBCConnectionException.class, () -> activityService.fetchAllCategories());
    }

    @Test
    void fetchAllCategories_whenJpaSystemException_throwsException() {
        // Arrange
        doThrow(new JpaSystemException(new RuntimeException("JPA system failure")))
                .when(activityRepository).getAllCategories();

        // Act & Assert
        assertThrows(JpaSystemException.class, () -> activityService.fetchAllCategories());
    }

    @Test
    void fetchAllCategories_whenTransactionException_throwsException() {
        // Arrange
        doThrow(new TransactionException("Transaction failure"))
                .when(activityRepository).getAllCategories();

        // Act & Assert
        assertThrows(TransactionException.class, () -> activityService.fetchAllCategories());
    }

    @Test
    void fetchAllCategories_whenUnexpectedException_throwsPersistenceException() {
        // Arrange
        doThrow(new RuntimeException("Unexpected error"))
                .when(activityRepository).getAllCategories();

        // Act & Assert
        PersistenceException exception = assertThrows(PersistenceException.class, () -> activityService.fetchAllCategories());
        assertEquals(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
    }

    private Activity createSampleActivity() {
        Activity activity = new Activity();
        activity.setActivity("Pool Table");
        activity.setCategory("Table Activity");
        activity.setPrice(4);  // This is where `intValue()` might be invoked
        activity.setImageLocation("/images/sample.jpg");
        activity.setCreatedTime(new Time(System.currentTimeMillis()));
        activity.setCreatedDate(new Date(System.currentTimeMillis()));
        activity.setLastUpdatedBy("user");
        activity.setAccessedBy("user");
        return activity;
    }
}
