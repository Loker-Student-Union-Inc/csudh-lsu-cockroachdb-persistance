package edu.csudh.lsu.persistence.service;

import edu.csudh.lsu.persistence.constants.PersistenceConstants;
import edu.csudh.lsu.persistence.exception.PersistenceException;
import edu.csudh.lsu.persistence.model.shift.ShiftTotal;
import edu.csudh.lsu.persistence.repository.gamesroom.shift.ShiftTotalRepository;
import org.hibernate.TransactionException;
import org.hibernate.exception.JDBCConnectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.jpa.JpaSystemException;

import java.sql.Date;
import java.sql.Time;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShiftTotalServiceTest {

    @Captor
    private ArgumentCaptor<UUID> uuidCaptor;

    @Captor
    private ArgumentCaptor<String> stringCaptor;

    @Captor
    private ArgumentCaptor<Date> dateCaptor;

    @Captor
    private ArgumentCaptor<Time> timeCaptor;

    @Mock
    private ShiftTotalRepository shiftTotalRepository;

    @InjectMocks
    private ShiftTotalService shiftTotalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test case for saveShiftTotal method
    @Test
    void saveShiftTotal_whenShiftTotalIsNull_logsWarning() {
        // Act
        shiftTotalService.saveShiftTotal(null);

        // Assert
        verify(shiftTotalRepository, never()).upsertShiftTotal(anyString(), anyString(), anyString(), anyFloat(), anyString(), any(), any(), any(), any(), any(), any(), any(), anyString(), anyString());
    }

    @Test
    void saveShiftTotal_whenValidShiftTotal_savesSuccessfully() {
        // Arrange
        ShiftTotal shiftTotal = createSampleShiftTotal();

        // Act
        shiftTotalService.saveShiftTotal(shiftTotal);

        // Assert
        verify(shiftTotalRepository, times(1)).upsertShiftTotal(
                eq(shiftTotal.getStudentName()), eq(shiftTotal.getAttendantName()), eq(shiftTotal.getActivity()),
                eq(shiftTotal.getCost()), eq(shiftTotal.getPaymentMode()), eq(shiftTotal.getStartTime()),
                eq(shiftTotal.getDate()), eq(shiftTotal.getDuration()), any(Time.class), any(Date.class),
                any(Date.class), any(Time.class), eq(shiftTotal.getLastUpdatedBy()), eq(shiftTotal.getAccessedBy())
        );
    }

    @Test
    void saveShiftTotal_whenDataAccessResourceFailureException_throwsException() {
        // Arrange
        ShiftTotal shiftTotal = createSampleShiftTotal();
        doThrow(new DataAccessResourceFailureException("Data access failure"))
                .when(shiftTotalRepository).upsertShiftTotal(anyString(), anyString(), anyString(), anyFloat(), anyString(), any(), any(), any(), any(), any(), any(), any(), anyString(), anyString());

        // Act & Assert
        assertThrows(DataAccessResourceFailureException.class, () -> shiftTotalService.saveShiftTotal(shiftTotal));
    }

    @Test
    void saveShiftTotal_whenJDBCConnectionException_throwsException() {
        // Arrange
        ShiftTotal shiftTotal = createSampleShiftTotal();
        doThrow(new JDBCConnectionException("JDBC connection failure", null))
                .when(shiftTotalRepository).upsertShiftTotal(anyString(), anyString(), anyString(), anyFloat(), anyString(), any(), any(), any(), any(), any(), any(), any(), anyString(), anyString());

        // Act & Assert
        assertThrows(JDBCConnectionException.class, () -> shiftTotalService.saveShiftTotal(shiftTotal));
    }

    @Test
    void saveShiftTotal_whenJpaSystemException_throwsException() {
        // Arrange
        ShiftTotal shiftTotal = createSampleShiftTotal();
        doThrow(new JpaSystemException(new RuntimeException("JPA system failure")))
                .when(shiftTotalRepository).upsertShiftTotal(anyString(), anyString(), anyString(), anyFloat(), anyString(), any(), any(), any(), any(), any(), any(), any(), anyString(), anyString());

        // Act & Assert
        assertThrows(JpaSystemException.class, () -> shiftTotalService.saveShiftTotal(shiftTotal));
    }

    @Test
    void saveShiftTotal_whenTransactionException_throwsException() {
        // Arrange
        ShiftTotal shiftTotal = createSampleShiftTotal();
        doThrow(new TransactionException("Transaction failure"))
                .when(shiftTotalRepository).upsertShiftTotal(anyString(), anyString(), anyString(), anyFloat(), anyString(), any(), any(), any(), any(), any(), any(), any(), anyString(), anyString());

        // Act & Assert
        assertThrows(TransactionException.class, () -> shiftTotalService.saveShiftTotal(shiftTotal));
    }

    @Test
    void saveShiftTotal_whenUnexpectedException_throwsPersistenceException() {
        // Arrange
        ShiftTotal shiftTotal = createSampleShiftTotal();
        doThrow(new RuntimeException("Unexpected error"))
                .when(shiftTotalRepository).upsertShiftTotal(anyString(), anyString(), anyString(), anyFloat(), anyString(), any(), any(), any(), any(), any(), any(), any(), anyString(), anyString());

        // Act & Assert
        PersistenceException exception = assertThrows(PersistenceException.class, () -> shiftTotalService.saveShiftTotal(shiftTotal));
        assertEquals(PersistenceConstants.AN_EXCEPTION_OCCURRED_WHILE_UPSERTING_A_RECORD, exception.getMessage());
    }

    // Test cases for deleteShiftTotal method
    @Test
    void deleteShiftTotal_whenValidId_deletesSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act
        shiftTotalService.deleteShiftTotal(id);

        // Assert
        verify(shiftTotalRepository, times(1)).deleteShiftTotal(eq(id));
    }

    @Test
    void deleteShiftTotal_whenDataAccessResourceFailureException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        doThrow(new DataAccessResourceFailureException("Data access failure"))
                .when(shiftTotalRepository).deleteShiftTotal(any(UUID.class));

        // Act & Assert
        assertThrows(DataAccessResourceFailureException.class, () -> shiftTotalService.deleteShiftTotal(id));
    }

    @Test
    void updateStudentName_whenDataAccessResourceFailureException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String studentName = "New Student Name";
        doThrow(new DataAccessResourceFailureException("Data access failure"))
                .when(shiftTotalRepository).updateStudentName(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(DataAccessResourceFailureException.class, () -> shiftTotalService.updateStudentName(id, studentName));
    }

    @Test
    void updateStudentName_whenJDBCConnectionException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String studentName = "New Student Name";
        doThrow(new JDBCConnectionException("JDBC connection failure", null))
                .when(shiftTotalRepository).updateStudentName(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(JDBCConnectionException.class, () -> shiftTotalService.updateStudentName(id, studentName));
    }

    @Test
    void updateStudentName_whenJpaSystemException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String studentName = "New Student Name";
        doThrow(new JpaSystemException(new RuntimeException("JPA system failure")))
                .when(shiftTotalRepository).updateStudentName(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(JpaSystemException.class, () -> shiftTotalService.updateStudentName(id, studentName));
    }

    @Test
    void updateStudentName_whenTransactionException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String studentName = "New Student Name";
        doThrow(new TransactionException("Transaction failure"))
                .when(shiftTotalRepository).updateStudentName(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(TransactionException.class, () -> shiftTotalService.updateStudentName(id, studentName));
    }

    @Test
    void updateStudentName_whenUnexpectedException_throwsPersistenceException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String studentName = "New Student Name";
        doThrow(new RuntimeException("Unexpected error"))
                .when(shiftTotalRepository).updateStudentName(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        PersistenceException exception = assertThrows(PersistenceException.class, () -> shiftTotalService.updateStudentName(id, studentName));
        assertEquals(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
    }

    // Helper method to mock current date and time
    private void mockCurrentDateTime() {
        Date currentDate = new Date(System.currentTimeMillis());
        Time currentTime = new Time(System.currentTimeMillis());

    }

    @Test
    void updateStudentName_whenValidData_updatesSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();
        String studentName = "New Student Name";
        Date currentDate = new Date(System.currentTimeMillis());
        Time currentTime = new Time(System.currentTimeMillis());

        // Act
        shiftTotalService.updateStudentName(id, studentName);

        // Assert
        verify(shiftTotalRepository, times(1)).updateStudentName(
                uuidCaptor.capture(),
                stringCaptor.capture(),
                dateCaptor.capture(),
                timeCaptor.capture(),
                stringCaptor.capture(),
                stringCaptor.capture()
        );

        // Validate captured arguments
        assertEquals(id, uuidCaptor.getValue());
        assertEquals(studentName, stringCaptor.getAllValues().get(0));  // The first string is studentName
        assertEquals(currentDate.toString(), dateCaptor.getValue().toString());  // Comparing the Date values as strings
        assertEquals(currentTime.toString(), timeCaptor.getValue().toString());  // Comparing the Time values as strings
        assertEquals("updatedByUser", stringCaptor.getAllValues().get(1));  // The second string is lastUpdatedBy
        assertEquals("accessedByUser", stringCaptor.getAllValues().get(2));  // The third string is accessedBy
    }

    @Test
    void deleteShiftTotal_whenJDBCConnectionException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        doThrow(new JDBCConnectionException("JDBC connection failure", null))
                .when(shiftTotalRepository).deleteShiftTotal(any(UUID.class));

        // Act & Assert
        assertThrows(JDBCConnectionException.class, () -> shiftTotalService.deleteShiftTotal(id));
    }

    @Test
    void deleteShiftTotal_whenJpaSystemException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        doThrow(new JpaSystemException(new RuntimeException("JPA system failure")))
                .when(shiftTotalRepository).deleteShiftTotal(any(UUID.class));

        // Act & Assert
        assertThrows(JpaSystemException.class, () -> shiftTotalService.deleteShiftTotal(id));
    }

    @Test
    void deleteShiftTotal_whenTransactionException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        doThrow(new TransactionException("Transaction failure"))
                .when(shiftTotalRepository).deleteShiftTotal(any(UUID.class));

        // Act & Assert
        assertThrows(TransactionException.class, () -> shiftTotalService.deleteShiftTotal(id));
    }

    @Test
    void deleteShiftTotal_whenUnexpectedException_throwsPersistenceException() {
        // Arrange
        UUID id = UUID.randomUUID();
        doThrow(new RuntimeException("Unexpected error"))
                .when(shiftTotalRepository).deleteShiftTotal(any(UUID.class));

        // Act & Assert
        PersistenceException exception = assertThrows(PersistenceException.class, () -> shiftTotalService.deleteShiftTotal(id));
        assertEquals(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
    }

    // Test cases for findAllShiftTotals method
    @Test
    void findAllShiftTotals_whenValidPageable_returnsShiftTotals() {
        // Arrange
        PageRequest pageable = PageRequest.of(0, 10);
        List<ShiftTotal> shiftTotalsList = Arrays.asList(createSampleShiftTotal(), createSampleShiftTotal());
        Page<ShiftTotal> shiftTotalsPage = new PageImpl<>(shiftTotalsList, pageable, shiftTotalsList.size());
        when(shiftTotalRepository.findAll(any(Pageable.class))).thenReturn(shiftTotalsPage);

        // Act
        Page<ShiftTotal> result = shiftTotalService.findAllShiftTotals(pageable);

        // Assert
        assertEquals(shiftTotalsPage.getTotalElements(), result.getTotalElements());
        verify(shiftTotalRepository, times(1)).findAll(eq(pageable));
    }

    @Test
    void findAllShiftTotals_whenDataAccessResourceFailureException_throwsException() {
        // Arrange
        PageRequest pageable = PageRequest.of(0, 10);
        doThrow(new DataAccessResourceFailureException("Data access failure"))
                .when(shiftTotalRepository).findAll(any(Pageable.class));

        // Act & Assert
        assertThrows(DataAccessResourceFailureException.class, () -> shiftTotalService.findAllShiftTotals(pageable));
    }

    @Test
    void findAllShiftTotals_whenJDBCConnectionException_throwsException() {
        // Arrange
        PageRequest pageable = PageRequest.of(0, 10);
        doThrow(new JDBCConnectionException("JDBC connection failure", null))
                .when(shiftTotalRepository).findAll(any(Pageable.class));

        // Act & Assert
        assertThrows(JDBCConnectionException.class, () -> shiftTotalService.findAllShiftTotals(pageable));
    }

    @Test
    void findAllShiftTotals_whenJpaSystemException_throwsException() {
        // Arrange
        PageRequest pageable = PageRequest.of(0, 10);
        doThrow(new JpaSystemException(new RuntimeException("JPA system failure")))
                .when(shiftTotalRepository).findAll(any(Pageable.class));

        // Act & Assert
        assertThrows(JpaSystemException.class, () -> shiftTotalService.findAllShiftTotals(pageable));
    }

    @Test
    void findAllShiftTotals_whenTransactionException_throwsException() {
        // Arrange
        PageRequest pageable = PageRequest.of(0, 10);
        doThrow(new TransactionException("Transaction failure"))
                .when(shiftTotalRepository).findAll(any(Pageable.class));

        // Act & Assert
        assertThrows(TransactionException.class, () -> shiftTotalService.findAllShiftTotals(pageable));
    }

    @Test
    void findAllShiftTotals_whenUnexpectedException_throwsPersistenceException() {
        // Arrange
        PageRequest pageable = PageRequest.of(0, 10);
        doThrow(new RuntimeException("Unexpected error"))
                .when(shiftTotalRepository).findAll(any(Pageable.class));

        // Act & Assert
        PersistenceException exception = assertThrows(PersistenceException.class, () -> shiftTotalService.findAllShiftTotals(pageable));
        assertEquals(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
    }

    // Test case for calculateTotalCostsByAttendantNameAndDate method
    @Test
    void calculateTotalCostsByAttendantNameAndDate_whenValidData_calculatesSuccessfully() {
        // Arrange
        String attendantName = "John Doe";
        Date date = new Date(System.currentTimeMillis());
        Object totalCosts = new Object();
        when(shiftTotalRepository.findTotalCostsByAttendantNameAndDate(anyString(), any(Date.class))).thenReturn(totalCosts);

        // Act
        Object result = shiftTotalService.calculateTotalCostsByAttendantNameAndDate(attendantName, date);

        // Assert
        assertEquals(totalCosts, result);
        verify(shiftTotalRepository, times(1)).findTotalCostsByAttendantNameAndDate(eq(attendantName), eq(date));
    }

    @Test
    void calculateTotalCostsByAttendantNameAndDate_whenDataAccessResourceFailureException_throwsException() {
        // Arrange
        String attendantName = "John Doe";
        Date date = new Date(System.currentTimeMillis());
        doThrow(new DataAccessResourceFailureException("Data access failure"))
                .when(shiftTotalRepository).findTotalCostsByAttendantNameAndDate(anyString(), any(Date.class));

        // Act & Assert
        assertThrows(DataAccessResourceFailureException.class, () -> shiftTotalService.calculateTotalCostsByAttendantNameAndDate(attendantName, date));
    }

    @Test
    void calculateTotalCostsByAttendantNameAndDate_whenJDBCConnectionException_throwsException() {
        // Arrange
        String attendantName = "John Doe";
        Date date = new Date(System.currentTimeMillis());
        doThrow(new JDBCConnectionException("JDBC connection failure", null))
                .when(shiftTotalRepository).findTotalCostsByAttendantNameAndDate(anyString(), any(Date.class));

        // Act & Assert
        assertThrows(JDBCConnectionException.class, () -> shiftTotalService.calculateTotalCostsByAttendantNameAndDate(attendantName, date));
    }

    @Test
    void calculateTotalCostsByAttendantNameAndDate_whenJpaSystemException_throwsException() {
        // Arrange
        String attendantName = "John Doe";
        Date date = new Date(System.currentTimeMillis());
        doThrow(new JpaSystemException(new RuntimeException("JPA system failure")))
                .when(shiftTotalRepository).findTotalCostsByAttendantNameAndDate(anyString(), any(Date.class));

        // Act & Assert
        assertThrows(JpaSystemException.class, () -> shiftTotalService.calculateTotalCostsByAttendantNameAndDate(attendantName, date));
    }

    @Test
    void calculateTotalCostsByAttendantNameAndDate_whenTransactionException_throwsException() {
        // Arrange
        String attendantName = "John Doe";
        Date date = new Date(System.currentTimeMillis());
        doThrow(new TransactionException("Transaction failure"))
                .when(shiftTotalRepository).findTotalCostsByAttendantNameAndDate(anyString(), any(Date.class));

        // Act & Assert
        assertThrows(TransactionException.class, () -> shiftTotalService.calculateTotalCostsByAttendantNameAndDate(attendantName, date));
    }

    @Test
    void calculateTotalCostsByAttendantNameAndDate_whenUnexpectedException_throwsPersistenceException() {
        // Arrange
        String attendantName = "John Doe";
        Date date = new Date(System.currentTimeMillis());
        doThrow(new RuntimeException("Unexpected error"))
                .when(shiftTotalRepository).findTotalCostsByAttendantNameAndDate(anyString(), any(Date.class));

        // Act & Assert
        PersistenceException exception = assertThrows(PersistenceException.class, () -> shiftTotalService.calculateTotalCostsByAttendantNameAndDate(attendantName, date));
        assertEquals(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
    }

    // Test cases for updateAttendantName method
    @Test
    void updateAttendantName_whenValidData_updatesSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();
        String attendantName = "New Attendant Name";

        // Act
        shiftTotalService.updateAttendantName(id, attendantName, "system", "system");

        // Assert
        verify(shiftTotalRepository, times(1)).updateAttendantName(eq(id), eq(attendantName), any(Date.class), any(Time.class), eq("system"), eq("system"));
    }

    @Test
    void updateAttendantName_whenDataAccessResourceFailureException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String attendantName = "New Attendant Name";
        doThrow(new DataAccessResourceFailureException("Data access failure"))
                .when(shiftTotalRepository).updateAttendantName(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(DataAccessResourceFailureException.class, () -> shiftTotalService.updateAttendantName(id, attendantName, "system", "system"));
    }

    @Test
    void updateAttendantName_whenJDBCConnectionException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String attendantName = "New Attendant Name";
        doThrow(new JDBCConnectionException("JDBC connection failure", null))
                .when(shiftTotalRepository).updateAttendantName(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(JDBCConnectionException.class, () -> shiftTotalService.updateAttendantName(id, attendantName, "system", "system"));
    }

    @Test
    void updateAttendantName_whenJpaSystemException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String attendantName = "New Attendant Name";
        doThrow(new JpaSystemException(new RuntimeException("JPA system failure")))
                .when(shiftTotalRepository).updateAttendantName(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(JpaSystemException.class, () -> shiftTotalService.updateAttendantName(id, attendantName, "system", "system"));
    }

    @Test
    void updateAttendantName_whenTransactionException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String attendantName = "New Attendant Name";
        doThrow(new TransactionException("Transaction failure"))
                .when(shiftTotalRepository).updateAttendantName(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(TransactionException.class, () -> shiftTotalService.updateAttendantName(id, attendantName, "system", "system"));
    }

    @Test
    void updateAttendantName_whenUnexpectedException_throwsPersistenceException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String attendantName = "New Attendant Name";
        doThrow(new RuntimeException("Unexpected error"))
                .when(shiftTotalRepository).updateAttendantName(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        PersistenceException exception = assertThrows(PersistenceException.class, () -> shiftTotalService.updateAttendantName(id, attendantName, "system", "system"));
        assertEquals(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
    }

    // Test cases for updateActivity method
    @Test
    void updateActivity_whenValidData_updatesSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();
        String activity = "New Activity";

        // Act
        shiftTotalService.updateActivity(id, activity, "system", "system");

        // Assert
        verify(shiftTotalRepository, times(1)).updateActivity(eq(id), eq(activity), any(Date.class), any(Time.class), eq("system"), eq("system"));
    }

    @Test
    void updateActivity_whenDataAccessResourceFailureException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String activity = "New Activity";
        doThrow(new DataAccessResourceFailureException("Data access failure"))
                .when(shiftTotalRepository).updateActivity(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(DataAccessResourceFailureException.class, () -> shiftTotalService.updateActivity(id, activity, "system", "system"));
    }

    @Test
    void updateActivity_whenJDBCConnectionException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String activity = "New Activity";
        doThrow(new JDBCConnectionException("JDBC connection failure", null))
                .when(shiftTotalRepository).updateActivity(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(JDBCConnectionException.class, () -> shiftTotalService.updateActivity(id, activity, "system", "system"));
    }

    @Test
    void updateActivity_whenJpaSystemException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String activity = "New Activity";
        doThrow(new JpaSystemException(new RuntimeException("JPA system failure")))
                .when(shiftTotalRepository).updateActivity(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(JpaSystemException.class, () -> shiftTotalService.updateActivity(id, activity, "system", "system"));
    }

    @Test
    void updateActivity_whenTransactionException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String activity = "New Activity";
        doThrow(new TransactionException("Transaction failure"))
                .when(shiftTotalRepository).updateActivity(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(TransactionException.class, () -> shiftTotalService.updateActivity(id, activity, "system", "system"));
    }

    @Test
    void updateActivity_whenUnexpectedException_throwsPersistenceException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String activity = "New Activity";
        doThrow(new RuntimeException("Unexpected error"))
                .when(shiftTotalRepository).updateActivity(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        PersistenceException exception = assertThrows(PersistenceException.class, () -> shiftTotalService.updateActivity(id, activity, "system", "system"));
        assertEquals(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
    }

    // Test cases for updateCost method
    @Test
    void updateCost_whenValidData_updatesSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();
        Float cost = 50.0f;

        // Act
        shiftTotalService.updateCost(id, cost, "system", "system");

        // Assert
        verify(shiftTotalRepository, times(1)).updateCost(eq(id), eq(cost), any(Date.class), any(Time.class), eq("system"), eq("system"));
    }

    @Test
    void updateCost_whenDataAccessResourceFailureException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        Float cost = 50.0f;
        doThrow(new DataAccessResourceFailureException("Data access failure"))
                .when(shiftTotalRepository).updateCost(any(UUID.class), anyFloat(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(DataAccessResourceFailureException.class, () -> shiftTotalService.updateCost(id, cost, "system", "system"));
    }

    @Test
    void updateCost_whenJDBCConnectionException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        Float cost = 50.0f;
        doThrow(new JDBCConnectionException("JDBC connection failure", null))
                .when(shiftTotalRepository).updateCost(any(UUID.class), anyFloat(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(JDBCConnectionException.class, () -> shiftTotalService.updateCost(id, cost, "system", "system"));
    }

    @Test
    void updateCost_whenJpaSystemException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        Float cost = 50.0f;
        doThrow(new JpaSystemException(new RuntimeException("JPA system failure")))
                .when(shiftTotalRepository).updateCost(any(UUID.class), anyFloat(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(JpaSystemException.class, () -> shiftTotalService.updateCost(id, cost, "system", "system"));
    }

    @Test
    void updateCost_whenTransactionException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        Float cost = 50.0f;
        doThrow(new TransactionException("Transaction failure"))
                .when(shiftTotalRepository).updateCost(any(UUID.class), anyFloat(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(TransactionException.class, () -> shiftTotalService.updateCost(id, cost, "system", "system"));
    }

    @Test
    void updateCost_whenUnexpectedException_throwsPersistenceException() {
        // Arrange
        UUID id = UUID.randomUUID();
        Float cost = 50.0f;
        doThrow(new RuntimeException("Unexpected error"))
                .when(shiftTotalRepository).updateCost(any(UUID.class), anyFloat(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        PersistenceException exception = assertThrows(PersistenceException.class, () -> shiftTotalService.updateCost(id, cost, "system", "system"));
        assertEquals(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
    }

    // Test cases for updatePaymentMode method
    @Test
    void updatePaymentMode_whenValidData_updatesSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();
        String paymentMode = "Credit Card";

        // Act
        shiftTotalService.updatePaymentMode(id, paymentMode, "system", "system");

        // Assert
        verify(shiftTotalRepository, times(1)).updatePaymentMode(eq(id), eq(paymentMode), any(Date.class), any(Time.class), eq("system"), eq("system"));
    }

    @Test
    void updatePaymentMode_whenDataAccessResourceFailureException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String paymentMode = "Credit Card";
        doThrow(new DataAccessResourceFailureException("Data access failure"))
                .when(shiftTotalRepository).updatePaymentMode(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(DataAccessResourceFailureException.class, () -> shiftTotalService.updatePaymentMode(id, paymentMode, "system", "system"));
    }

    @Test
    void updatePaymentMode_whenJDBCConnectionException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String paymentMode = "Credit Card";
        doThrow(new JDBCConnectionException("JDBC connection failure", null))
                .when(shiftTotalRepository).updatePaymentMode(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(JDBCConnectionException.class, () -> shiftTotalService.updatePaymentMode(id, paymentMode, "system", "system"));
    }

    @Test
    void updatePaymentMode_whenJpaSystemException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String paymentMode = "Credit Card";
        doThrow(new JpaSystemException(new RuntimeException("JPA system failure")))
                .when(shiftTotalRepository).updatePaymentMode(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(JpaSystemException.class, () -> shiftTotalService.updatePaymentMode(id, paymentMode, "system", "system"));
    }

    @Test
    void updatePaymentMode_whenTransactionException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String paymentMode = "Credit Card";
        doThrow(new TransactionException("Transaction failure"))
                .when(shiftTotalRepository).updatePaymentMode(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(TransactionException.class, () -> shiftTotalService.updatePaymentMode(id, paymentMode, "system", "system"));
    }

    @Test
    void updatePaymentMode_whenUnexpectedException_throwsPersistenceException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String paymentMode = "Credit Card";
        doThrow(new RuntimeException("Unexpected error"))
                .when(shiftTotalRepository).updatePaymentMode(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        PersistenceException exception = assertThrows(PersistenceException.class, () -> shiftTotalService.updatePaymentMode(id, paymentMode, "system", "system"));
        assertEquals(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
    }

    // Test cases for updateDuration method
    @Test
    void updateDuration_whenValidData_updatesSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();
        String duration = "2 hours";

        // Act
        shiftTotalService.updateDuration(id, duration, "system", "system");

        // Assert
        verify(shiftTotalRepository, times(1)).updateDuration(eq(id), eq(duration), any(Date.class), any(Time.class), eq("system"), eq("system"));
    }

    @Test
    void updateDuration_whenDataAccessResourceFailureException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String duration = "2 hours";
        doThrow(new DataAccessResourceFailureException("Data access failure"))
                .when(shiftTotalRepository).updateDuration(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(DataAccessResourceFailureException.class, () -> shiftTotalService.updateDuration(id, duration, "system", "system"));
    }

    @Test
    void updateDuration_whenJDBCConnectionException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String duration = "2 hours";
        doThrow(new JDBCConnectionException("JDBC connection failure", null))
                .when(shiftTotalRepository).updateDuration(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(JDBCConnectionException.class, () -> shiftTotalService.updateDuration(id, duration, "system", "system"));
    }

    @Test
    void updateDuration_whenJpaSystemException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String duration = "2 hours";
        doThrow(new JpaSystemException(new RuntimeException("JPA system failure")))
                .when(shiftTotalRepository).updateDuration(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(JpaSystemException.class, () -> shiftTotalService.updateDuration(id, duration, "system", "system"));
    }

    @Test
    void updateDuration_whenTransactionException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String duration = "2 hours";
        doThrow(new TransactionException("Transaction failure"))
                .when(shiftTotalRepository).updateDuration(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(TransactionException.class, () -> shiftTotalService.updateDuration(id, duration, "system", "system"));
    }

    @Test
    void updateDuration_whenUnexpectedException_throwsPersistenceException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String duration = "2 hours";
        doThrow(new RuntimeException("Unexpected error"))
                .when(shiftTotalRepository).updateDuration(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        PersistenceException exception = assertThrows(PersistenceException.class, () -> shiftTotalService.updateDuration(id, duration, "system", "system"));
        assertEquals(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
    }

    // Helper method to create a sample ShiftTotal object
    private ShiftTotal createSampleShiftTotal() {
        ShiftTotal shiftTotal = new ShiftTotal();
        shiftTotal.setStudentName("John Doe");
        shiftTotal.setAttendantName("Jane Smith");
        shiftTotal.setActivity("Bowling");
        shiftTotal.setCost(50.0f);
        shiftTotal.setPaymentMode("Credit Card");
        shiftTotal.setDuration("1 hour");
        shiftTotal.setCreatedTime(new Time(System.currentTimeMillis()));
        shiftTotal.setCreatedDate(new Date(System.currentTimeMillis()));
        shiftTotal.setLastUpdatedBy("system");
        shiftTotal.setAccessedBy("system");
        return shiftTotal;
    }
}
