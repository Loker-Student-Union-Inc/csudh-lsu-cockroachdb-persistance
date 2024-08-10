package edu.csudh.lsu.persistence.service;

import edu.csudh.lsu.persistence.constants.PersistenceConstants;
import edu.csudh.lsu.persistence.exception.PersistenceException;
import edu.csudh.lsu.persistence.model.shift.ShiftReport;
import edu.csudh.lsu.persistence.repository.gamesroom.shift.ShiftReportRepository;
import edu.csudh.lsu.persistence.utils.TimeUtils;
import org.hibernate.TransactionException;
import org.hibernate.exception.JDBCConnectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

class ShiftReportServiceTest {

    @Mock
    private ShiftReportRepository shiftReportRepository;

    @InjectMocks
    private ShiftReportService shiftReportService;

    @Captor
    private ArgumentCaptor<UUID> uuidCaptor;

    @Captor
    private ArgumentCaptor<String> stringCaptor;

    @Captor
    private ArgumentCaptor<Date> dateCaptor;

    @Captor
    private ArgumentCaptor<Time> timeCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test case for saveOrUpdateShiftReport method
    @Test
    void saveOrUpdateShiftReport_whenValidShiftReport_savesSuccessfully() {
        // Arrange
        ShiftReport shiftReport = createSampleShiftReport();

        // Act
        shiftReportService.saveOrUpdateShiftReport(shiftReport);

        // Assert
        verify(shiftReportRepository, times(1)).upsertShiftReport(
                eq(shiftReport.getClosingShiftDate()), eq(shiftReport.getClosingShiftTime()), eq(shiftReport.getAttendantName()),
                eq(shiftReport.getReconcilorName()), eq(shiftReport.getReconcilorSign()), eq(shiftReport.getAttendantSign()),
                eq(shiftReport.getRevenueInCard()), eq(shiftReport.getRevenueInCash()), eq(shiftReport.getShiftTotal()),
                eq(shiftReport.getOpeningBalance()), any(Time.class), any(Date.class),
                any(Date.class), any(Time.class),
                eq(shiftReport.getLastUpdatedBy()), eq(shiftReport.getAccessedBy())
        );
    }

    @Test
    void saveOrUpdateShiftReport_whenShiftReportIsNull_logsWarning() {
        // Act
        shiftReportService.saveOrUpdateShiftReport(null);

        // Assert
        verify(shiftReportRepository, never()).upsertShiftReport(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void saveOrUpdateShiftReport_whenDataAccessResourceFailureException_throwsException() {
        // Arrange
        ShiftReport shiftReport = createSampleShiftReport();
        doThrow(new DataAccessResourceFailureException("Data access failure"))
                .when(shiftReportRepository).upsertShiftReport(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());

        // Act & Assert
        assertThrows(DataAccessResourceFailureException.class, () -> shiftReportService.saveOrUpdateShiftReport(shiftReport));
    }

    @Test
    void saveOrUpdateShiftReport_whenJDBCConnectionException_throwsException() {
        // Arrange
        ShiftReport shiftReport = createSampleShiftReport();
        doThrow(new JDBCConnectionException("JDBC connection failure", null))
                .when(shiftReportRepository).upsertShiftReport(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());

        // Act & Assert
        assertThrows(JDBCConnectionException.class, () -> shiftReportService.saveOrUpdateShiftReport(shiftReport));
    }

    @Test
    void saveOrUpdateShiftReport_whenJpaSystemException_throwsException() {
        // Arrange
        ShiftReport shiftReport = createSampleShiftReport();
        doThrow(new JpaSystemException(new RuntimeException("JPA system failure")))
                .when(shiftReportRepository).upsertShiftReport(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());

        // Act & Assert
        assertThrows(JpaSystemException.class, () -> shiftReportService.saveOrUpdateShiftReport(shiftReport));
    }

    @Test
    void saveOrUpdateShiftReport_whenTransactionException_throwsException() {
        // Arrange
        ShiftReport shiftReport = createSampleShiftReport();
        doThrow(new TransactionException("Transaction failure"))
                .when(shiftReportRepository).upsertShiftReport(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());

        // Act & Assert
        assertThrows(TransactionException.class, () -> shiftReportService.saveOrUpdateShiftReport(shiftReport));
    }

    @Test
    void saveOrUpdateShiftReport_whenUnexpectedException_throwsPersistenceException() {
        // Arrange
        ShiftReport shiftReport = createSampleShiftReport();
        doThrow(new RuntimeException("Unexpected error"))
                .when(shiftReportRepository).upsertShiftReport(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());

        // Act & Assert
        PersistenceException exception = assertThrows(PersistenceException.class, () -> shiftReportService.saveOrUpdateShiftReport(shiftReport));
        assertEquals(PersistenceConstants.AN_EXCEPTION_OCCURRED_WHILE_UPSERTING_A_RECORD, exception.getMessage());
    }

    // Test case for updateClosingShiftDate method
    @Test
    void updateClosingShiftDate_whenValidData_updatesSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();
        Date closingShiftDate = new Date(System.currentTimeMillis());

        // Act
        shiftReportService.updateClosingShiftDate(id, closingShiftDate, "system", "system");

        // Assert
        verify(shiftReportRepository, times(1)).updateClosingShiftDate(
                eq(id), eq(closingShiftDate), any(Date.class), any(Time.class), eq("system"), eq("system"));
    }

    @Test
    void updateClosingShiftDate_whenDataAccessResourceFailureException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        Date closingShiftDate = new Date(System.currentTimeMillis());
        doThrow(new DataAccessResourceFailureException("Data access failure"))
                .when(shiftReportRepository).updateClosingShiftDate(any(UUID.class), any(Date.class), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(DataAccessResourceFailureException.class, () -> shiftReportService.updateClosingShiftDate(id, closingShiftDate, "system", "system"));
    }

    @Test
    void updateClosingShiftDate_whenJDBCConnectionException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        Date closingShiftDate = new Date(System.currentTimeMillis());
        doThrow(new JDBCConnectionException("JDBC connection failure", null))
                .when(shiftReportRepository).updateClosingShiftDate(any(UUID.class), any(Date.class), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(JDBCConnectionException.class, () -> shiftReportService.updateClosingShiftDate(id, closingShiftDate, "system", "system"));
    }

    @Test
    void updateClosingShiftDate_whenJpaSystemException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        Date closingShiftDate = new Date(System.currentTimeMillis());
        doThrow(new JpaSystemException(new RuntimeException("JPA system failure")))
                .when(shiftReportRepository).updateClosingShiftDate(any(UUID.class), any(Date.class), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(JpaSystemException.class, () -> shiftReportService.updateClosingShiftDate(id, closingShiftDate, "system", "system"));
    }

    @Test
    void updateClosingShiftDate_whenTransactionException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        Date closingShiftDate = new Date(System.currentTimeMillis());
        doThrow(new TransactionException("Transaction failure"))
                .when(shiftReportRepository).updateClosingShiftDate(any(UUID.class), any(Date.class), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(TransactionException.class, () -> shiftReportService.updateClosingShiftDate(id, closingShiftDate, "system", "system"));
    }

    @Test
    void updateClosingShiftDate_whenUnexpectedException_throwsPersistenceException() {
        // Arrange
        UUID id = UUID.randomUUID();
        Date closingShiftDate = new Date(System.currentTimeMillis());
        doThrow(new RuntimeException("Unexpected error"))
                .when(shiftReportRepository).updateClosingShiftDate(any(UUID.class), any(Date.class), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        PersistenceException exception = assertThrows(PersistenceException.class, () -> shiftReportService.updateClosingShiftDate(id, closingShiftDate, "system", "system"));
        assertEquals(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
    }

    // Test case for updateClosingShiftTime method
    @Test
    void updateClosingShiftTime_whenValidData_updatesSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();
        Time closingShiftTime = new Time(System.currentTimeMillis());

        // Act
        shiftReportService.updateClosingShiftTime(id, closingShiftTime, "system", "system");

        // Assert
        verify(shiftReportRepository, times(1)).updateClosingShiftTime(
                eq(id), eq(closingShiftTime), any(Date.class), any(Time.class), eq("system"), eq("system"));
    }

    @Test
    void updateClosingShiftTime_whenDataAccessResourceFailureException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        Time closingShiftTime = new Time(System.currentTimeMillis());
        doThrow(new DataAccessResourceFailureException("Data access failure"))
                .when(shiftReportRepository).updateClosingShiftTime(any(UUID.class), any(Time.class), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(DataAccessResourceFailureException.class, () -> shiftReportService.updateClosingShiftTime(id, closingShiftTime, "system", "system"));
    }

    @Test
    void updateClosingShiftTime_whenJDBCConnectionException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        Time closingShiftTime = new Time(System.currentTimeMillis());
        doThrow(new JDBCConnectionException("JDBC connection failure", null))
                .when(shiftReportRepository).updateClosingShiftTime(any(UUID.class), any(Time.class), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(JDBCConnectionException.class, () -> shiftReportService.updateClosingShiftTime(id, closingShiftTime, "system", "system"));
    }

    @Test
    void updateClosingShiftTime_whenJpaSystemException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        Time closingShiftTime = new Time(System.currentTimeMillis());
        doThrow(new JpaSystemException(new RuntimeException("JPA system failure")))
                .when(shiftReportRepository).updateClosingShiftTime(any(UUID.class), any(Time.class), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(JpaSystemException.class, () -> shiftReportService.updateClosingShiftTime(id, closingShiftTime, "system", "system"));
    }

    @Test
    void updateClosingShiftTime_whenTransactionException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        Time closingShiftTime = new Time(System.currentTimeMillis());
        doThrow(new TransactionException("Transaction failure"))
                .when(shiftReportRepository).updateClosingShiftTime(any(UUID.class), any(Time.class), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(TransactionException.class, () -> shiftReportService.updateClosingShiftTime(id, closingShiftTime, "system", "system"));
    }

    @Test
    void updateClosingShiftTime_whenUnexpectedException_throwsPersistenceException() {
        // Arrange
        UUID id = UUID.randomUUID();
        Time closingShiftTime = new Time(System.currentTimeMillis());
        doThrow(new RuntimeException("Unexpected error"))
                .when(shiftReportRepository).updateClosingShiftTime(any(UUID.class), any(Time.class), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        PersistenceException exception = assertThrows(PersistenceException.class, () -> shiftReportService.updateClosingShiftTime(id, closingShiftTime, "system", "system"));
        assertEquals(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
    }

    // Test case for deleteShiftReport method
    @Test
    void deleteShiftReport_whenValidId_deletesSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act
        shiftReportService.deleteShiftReport(id);

        // Assert
        verify(shiftReportRepository, times(1)).deleteShiftReport(eq(id));
    }

    @Test
    void deleteShiftReport_whenDataAccessResourceFailureException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        doThrow(new DataAccessResourceFailureException("Data access failure"))
                .when(shiftReportRepository).deleteShiftReport(any(UUID.class));

        // Act & Assert
        assertThrows(DataAccessResourceFailureException.class, () -> shiftReportService.deleteShiftReport(id));
    }

    @Test
    void deleteShiftReport_whenJDBCConnectionException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        doThrow(new JDBCConnectionException("JDBC connection failure", null))
                .when(shiftReportRepository).deleteShiftReport(any(UUID.class));

        // Act & Assert
        assertThrows(JDBCConnectionException.class, () -> shiftReportService.deleteShiftReport(id));
    }

    @Test
    void deleteShiftReport_whenJpaSystemException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        doThrow(new JpaSystemException(new RuntimeException("JPA system failure")))
                .when(shiftReportRepository).deleteShiftReport(any(UUID.class));

        // Act & Assert
        assertThrows(JpaSystemException.class, () -> shiftReportService.deleteShiftReport(id));
    }

    @Test
    void deleteShiftReport_whenTransactionException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        doThrow(new TransactionException("Transaction failure"))
                .when(shiftReportRepository).deleteShiftReport(any(UUID.class));

        // Act & Assert
        assertThrows(TransactionException.class, () -> shiftReportService.deleteShiftReport(id));
    }

    @Test
    void deleteShiftReport_whenUnexpectedException_throwsPersistenceException() {
        // Arrange
        UUID id = UUID.randomUUID();
        doThrow(new RuntimeException("Unexpected error"))
                .when(shiftReportRepository).deleteShiftReport(any(UUID.class));

        // Act & Assert
        PersistenceException exception = assertThrows(PersistenceException.class, () -> shiftReportService.deleteShiftReport(id));
        assertEquals(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
    }

    // Test case for updateAttendantName method
    @Test
    void updateAttendantName_whenValidData_updatesSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();
        String attendantName = "New Attendant Name";

        // Act
        shiftReportService.updateAttendantName(id, attendantName, "system", "system");

        // Assert
        verify(shiftReportRepository, times(1)).updateAttendantName(eq(id), eq(attendantName), any(Date.class), any(Time.class), eq("system"), eq("system"));
    }

    @Test
    void updateAttendantName_whenDataAccessResourceFailureException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String attendantName = "New Attendant Name";
        doThrow(new DataAccessResourceFailureException("Data access failure"))
                .when(shiftReportRepository).updateAttendantName(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(DataAccessResourceFailureException.class, () -> shiftReportService.updateAttendantName(id, attendantName, "system", "system"));
    }

    @Test
    void updateAttendantName_whenJDBCConnectionException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String attendantName = "New Attendant Name";
        doThrow(new JDBCConnectionException("JDBC connection failure", null))
                .when(shiftReportRepository).updateAttendantName(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(JDBCConnectionException.class, () -> shiftReportService.updateAttendantName(id, attendantName, "system", "system"));
    }

    @Test
    void updateAttendantName_whenJpaSystemException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String attendantName = "New Attendant Name";
        doThrow(new JpaSystemException(new RuntimeException("JPA system failure")))
                .when(shiftReportRepository).updateAttendantName(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(JpaSystemException.class, () -> shiftReportService.updateAttendantName(id, attendantName, "system", "system"));
    }

    @Test
    void updateAttendantName_whenTransactionException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String attendantName = "New Attendant Name";
        doThrow(new TransactionException("Transaction failure"))
                .when(shiftReportRepository).updateAttendantName(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(TransactionException.class, () -> shiftReportService.updateAttendantName(id, attendantName, "system", "system"));
    }

    @Test
    void updateAttendantName_whenUnexpectedException_throwsPersistenceException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String attendantName = "New Attendant Name";
        doThrow(new RuntimeException("Unexpected error"))
                .when(shiftReportRepository).updateAttendantName(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        PersistenceException exception = assertThrows(PersistenceException.class, () -> shiftReportService.updateAttendantName(id, attendantName, "system", "system"));
        assertEquals(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
    }

    // Test case for updateReconcilorName method
    @Test
    void updateReconcilorName_whenValidData_updatesSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();
        String reconcilorName = "New Reconcilor Name";

        // Act
        shiftReportService.updateReconcilorName(id, reconcilorName, "system", "system");

        // Assert
        verify(shiftReportRepository, times(1)).updateReconcilorName(eq(id), eq(reconcilorName), any(Date.class), any(Time.class), eq("system"), eq("system"));
    }

    @Test
    void updateReconcilorName_whenDataAccessResourceFailureException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String reconcilorName = "New Reconcilor Name";
        doThrow(new DataAccessResourceFailureException("Data access failure"))
                .when(shiftReportRepository).updateReconcilorName(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(DataAccessResourceFailureException.class, () -> shiftReportService.updateReconcilorName(id, reconcilorName, "system", "system"));
    }

    @Test
    void updateReconcilorName_whenJDBCConnectionException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String reconcilorName = "New Reconcilor Name";
        doThrow(new JDBCConnectionException("JDBC connection failure", null))
                .when(shiftReportRepository).updateReconcilorName(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(JDBCConnectionException.class, () -> shiftReportService.updateReconcilorName(id, reconcilorName, "system", "system"));
    }

    @Test
    void updateReconcilorName_whenJpaSystemException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String reconcilorName = "New Reconcilor Name";
        doThrow(new JpaSystemException(new RuntimeException("JPA system failure")))
                .when(shiftReportRepository).updateReconcilorName(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(JpaSystemException.class, () -> shiftReportService.updateReconcilorName(id, reconcilorName, "system", "system"));
    }

    @Test
    void updateReconcilorName_whenTransactionException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String reconcilorName = "New Reconcilor Name";
        doThrow(new TransactionException("Transaction failure"))
                .when(shiftReportRepository).updateReconcilorName(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(TransactionException.class, () -> shiftReportService.updateReconcilorName(id, reconcilorName, "system", "system"));
    }

    @Test
    void updateReconcilorName_whenUnexpectedException_throwsPersistenceException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String reconcilorName = "New Reconcilor Name";
        doThrow(new RuntimeException("Unexpected error"))
                .when(shiftReportRepository).updateReconcilorName(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        PersistenceException exception = assertThrows(PersistenceException.class, () -> shiftReportService.updateReconcilorName(id, reconcilorName, "system", "system"));
        assertEquals(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
    }

    // Test case for updateReconcilorSign method
    @Test
    void updateReconcilorSign_whenValidData_updatesSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();
        String reconcilorSign = "New Reconcilor Sign";

        // Act
        shiftReportService.updateReconcilorSign(id, reconcilorSign, "system", "system");

        // Assert
        verify(shiftReportRepository, times(1)).updateReconcilorSign(eq(id), eq(reconcilorSign), any(Date.class), any(Time.class), eq("system"), eq("system"));
    }

    @Test
    void updateReconcilorSign_whenDataAccessResourceFailureException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String reconcilorSign = "New Reconcilor Sign";
        doThrow(new DataAccessResourceFailureException("Data access failure"))
                .when(shiftReportRepository).updateReconcilorSign(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(DataAccessResourceFailureException.class, () -> shiftReportService.updateReconcilorSign(id, reconcilorSign, "system", "system"));
    }

    @Test
    void updateReconcilorSign_whenJDBCConnectionException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String reconcilorSign = "New Reconcilor Sign";
        doThrow(new JDBCConnectionException("JDBC connection failure", null))
                .when(shiftReportRepository).updateReconcilorSign(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(JDBCConnectionException.class, () -> shiftReportService.updateReconcilorSign(id, reconcilorSign, "system", "system"));
    }

    @Test
    void updateReconcilorSign_whenJpaSystemException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String reconcilorSign = "New Reconcilor Sign";
        doThrow(new JpaSystemException(new RuntimeException("JPA system failure")))
                .when(shiftReportRepository).updateReconcilorSign(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(JpaSystemException.class, () -> shiftReportService.updateReconcilorSign(id, reconcilorSign, "system", "system"));
    }

    @Test
    void updateReconcilorSign_whenTransactionException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String reconcilorSign = "New Reconcilor Sign";
        doThrow(new TransactionException("Transaction failure"))
                .when(shiftReportRepository).updateReconcilorSign(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(TransactionException.class, () -> shiftReportService.updateReconcilorSign(id, reconcilorSign, "system", "system"));
    }

    @Test
    void updateReconcilorSign_whenUnexpectedException_throwsPersistenceException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String reconcilorSign = "New Reconcilor Sign";
        doThrow(new RuntimeException("Unexpected error"))
                .when(shiftReportRepository).updateReconcilorSign(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        PersistenceException exception = assertThrows(PersistenceException.class, () -> shiftReportService.updateReconcilorSign(id, reconcilorSign, "system", "system"));
        assertEquals(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
    }

    // Test case for updateAttendantSign method
    @Test
    void updateAttendantSign_whenValidData_updatesSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();
        String attendantSign = "New Attendant Sign";

        // Act
        shiftReportService.updateAttendantSign(id, attendantSign, "system", "system");

        // Assert
        verify(shiftReportRepository, times(1)).updateAttendantSign(eq(id), eq(attendantSign), any(Date.class), any(Time.class), eq("system"), eq("system"));
    }

    @Test
    void updateAttendantSign_whenDataAccessResourceFailureException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String attendantSign = "New Attendant Sign";
        doThrow(new DataAccessResourceFailureException("Data access failure"))
                .when(shiftReportRepository).updateAttendantSign(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(DataAccessResourceFailureException.class, () -> shiftReportService.updateAttendantSign(id, attendantSign, "system", "system"));
    }

    @Test
    void updateAttendantSign_whenJDBCConnectionException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String attendantSign = "New Attendant Sign";
        doThrow(new JDBCConnectionException("JDBC connection failure", null))
                .when(shiftReportRepository).updateAttendantSign(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(JDBCConnectionException.class, () -> shiftReportService.updateAttendantSign(id, attendantSign, "system", "system"));
    }

    @Test
    void updateAttendantSign_whenJpaSystemException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String attendantSign = "New Attendant Sign";
        doThrow(new JpaSystemException(new RuntimeException("JPA system failure")))
                .when(shiftReportRepository).updateAttendantSign(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(JpaSystemException.class, () -> shiftReportService.updateAttendantSign(id, attendantSign, "system", "system"));
    }

    @Test
    void updateAttendantSign_whenTransactionException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String attendantSign = "New Attendant Sign";
        doThrow(new TransactionException("Transaction failure"))
                .when(shiftReportRepository).updateAttendantSign(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(TransactionException.class, () -> shiftReportService.updateAttendantSign(id, attendantSign, "system", "system"));
    }

    @Test
    void updateAttendantSign_whenUnexpectedException_throwsPersistenceException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String attendantSign = "New Attendant Sign";
        doThrow(new RuntimeException("Unexpected error"))
                .when(shiftReportRepository).updateAttendantSign(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        PersistenceException exception = assertThrows(PersistenceException.class, () -> shiftReportService.updateAttendantSign(id, attendantSign, "system", "system"));
        assertEquals(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
    }

    // Test case for updateRevenueInCard method
    @Test
    void updateRevenueInCard_whenValidData_updatesSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();
        Float revenueInCard = 100.0f;

        // Act
        shiftReportService.updateRevenueInCard(id, revenueInCard, "system", "system");

        // Assert
        verify(shiftReportRepository, times(1)).updateRevenueInCard(eq(id), eq(revenueInCard), any(Date.class), any(Time.class), eq("system"), eq("system"));
    }

    @Test
    void updateRevenueInCard_whenDataAccessResourceFailureException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        Float revenueInCard = 100.0f;
        doThrow(new DataAccessResourceFailureException("Data access failure"))
                .when(shiftReportRepository).updateRevenueInCard(any(UUID.class), anyFloat(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(DataAccessResourceFailureException.class, () -> shiftReportService.updateRevenueInCard(id, revenueInCard, "system", "system"));
    }

    @Test
    void updateRevenueInCard_whenJDBCConnectionException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        Float revenueInCard = 100.0f;
        doThrow(new JDBCConnectionException("JDBC connection failure", null))
                .when(shiftReportRepository).updateRevenueInCard(any(UUID.class), anyFloat(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(JDBCConnectionException.class, () -> shiftReportService.updateRevenueInCard(id, revenueInCard, "system", "system"));
    }

    @Test
    void updateRevenueInCard_whenJpaSystemException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        Float revenueInCard = 100.0f;
        doThrow(new JpaSystemException(new RuntimeException("JPA system failure")))
                .when(shiftReportRepository).updateRevenueInCard(any(UUID.class), anyFloat(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(JpaSystemException.class, () -> shiftReportService.updateRevenueInCard(id, revenueInCard, "system", "system"));
    }

    @Test
    void updateRevenueInCard_whenTransactionException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        Float revenueInCard = 100.0f;
        doThrow(new TransactionException("Transaction failure"))
                .when(shiftReportRepository).updateRevenueInCard(any(UUID.class), anyFloat(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(TransactionException.class, () -> shiftReportService.updateRevenueInCard(id, revenueInCard, "system", "system"));
    }

    @Test
    void updateRevenueInCard_whenUnexpectedException_throwsPersistenceException() {
        // Arrange
        UUID id = UUID.randomUUID();
        Float revenueInCard = 100.0f;
        doThrow(new RuntimeException("Unexpected error"))
                .when(shiftReportRepository).updateRevenueInCard(any(UUID.class), anyFloat(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        PersistenceException exception = assertThrows(PersistenceException.class, () -> shiftReportService.updateRevenueInCard(id, revenueInCard, "system", "system"));
        assertEquals(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
    }

    // Test case for updateRevenueInCash method
    @Test
    void updateRevenueInCash_whenValidData_updatesSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();
        Float revenueInCash = 200.0f;

        // Act
        shiftReportService.updateRevenueInCash(id, revenueInCash, "system", "system");

        // Assert
        verify(shiftReportRepository, times(1)).updateRevenueInCash(eq(id), eq(revenueInCash), any(Date.class), any(Time.class), eq("system"), eq("system"));
    }

    @Test
    void updateRevenueInCash_whenDataAccessResourceFailureException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        Float revenueInCash = 200.0f;
        doThrow(new DataAccessResourceFailureException("Data access failure"))
                .when(shiftReportRepository).updateRevenueInCash(any(UUID.class), anyFloat(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(DataAccessResourceFailureException.class, () -> shiftReportService.updateRevenueInCash(id, revenueInCash, "system", "system"));
    }

    @Test
    void updateRevenueInCash_whenJDBCConnectionException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        Float revenueInCash = 200.0f;
        doThrow(new JDBCConnectionException("JDBC connection failure", null))
                .when(shiftReportRepository).updateRevenueInCash(any(UUID.class), anyFloat(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(JDBCConnectionException.class, () -> shiftReportService.updateRevenueInCash(id, revenueInCash, "system", "system"));
    }

    @Test
    void updateRevenueInCash_whenJpaSystemException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        Float revenueInCash = 200.0f;
        doThrow(new JpaSystemException(new RuntimeException("JPA system failure")))
                .when(shiftReportRepository).updateRevenueInCash(any(UUID.class), anyFloat(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(JpaSystemException.class, () -> shiftReportService.updateRevenueInCash(id, revenueInCash, "system", "system"));
    }

    @Test
    void updateRevenueInCash_whenTransactionException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        Float revenueInCash = 200.0f;
        doThrow(new TransactionException("Transaction failure"))
                .when(shiftReportRepository).updateRevenueInCash(any(UUID.class), anyFloat(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(TransactionException.class, () -> shiftReportService.updateRevenueInCash(id, revenueInCash, "system", "system"));
    }

    @Test
    void updateRevenueInCash_whenUnexpectedException_throwsPersistenceException() {
        // Arrange
        UUID id = UUID.randomUUID();
        Float revenueInCash = 200.0f;
        doThrow(new RuntimeException("Unexpected error"))
                .when(shiftReportRepository).updateRevenueInCash(any(UUID.class), anyFloat(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        PersistenceException exception = assertThrows(PersistenceException.class, () -> shiftReportService.updateRevenueInCash(id, revenueInCash, "system", "system"));
        assertEquals(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
    }

    // Test case for updateShiftTotal method
    @Test
    void updateShiftTotal_whenValidData_updatesSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();
        String shiftTotal = "1000";

        // Act
        shiftReportService.updateShiftTotal(id, shiftTotal, "system", "system");

        // Assert
        verify(shiftReportRepository, times(1)).updateShiftTotal(eq(id), eq(shiftTotal), any(Date.class), any(Time.class), eq("system"), eq("system"));
    }

    @Test
    void updateShiftTotal_whenDataAccessResourceFailureException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String shiftTotal = "1000";
        doThrow(new DataAccessResourceFailureException("Data access failure"))
                .when(shiftReportRepository).updateShiftTotal(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(DataAccessResourceFailureException.class, () -> shiftReportService.updateShiftTotal(id, shiftTotal, "system", "system"));
    }

    @Test
    void updateShiftTotal_whenJDBCConnectionException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String shiftTotal = "1000";
        doThrow(new JDBCConnectionException("JDBC connection failure", null))
                .when(shiftReportRepository).updateShiftTotal(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(JDBCConnectionException.class, () -> shiftReportService.updateShiftTotal(id, shiftTotal, "system", "system"));
    }

    @Test
    void updateShiftTotal_whenJpaSystemException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String shiftTotal = "1000";
        doThrow(new JpaSystemException(new RuntimeException("JPA system failure")))
                .when(shiftReportRepository).updateShiftTotal(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(JpaSystemException.class, () -> shiftReportService.updateShiftTotal(id, shiftTotal, "system", "system"));
    }

    @Test
    void updateShiftTotal_whenTransactionException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String shiftTotal = "1000";
        doThrow(new TransactionException("Transaction failure"))
                .when(shiftReportRepository).updateShiftTotal(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(TransactionException.class, () -> shiftReportService.updateShiftTotal(id, shiftTotal, "system", "system"));
    }

    @Test
    void updateShiftTotal_whenUnexpectedException_throwsPersistenceException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String shiftTotal = "1000";
        doThrow(new RuntimeException("Unexpected error"))
                .when(shiftReportRepository).updateShiftTotal(any(UUID.class), anyString(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        PersistenceException exception = assertThrows(PersistenceException.class, () -> shiftReportService.updateShiftTotal(id, shiftTotal, "system", "system"));
        assertEquals(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
    }

    // Test case for updateOpeningBalance method
    @Test
    void updateOpeningBalance_whenValidData_updatesSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();
        Float openingBalance = 500.0f;

        // Act
        shiftReportService.updateOpeningBalance(id, openingBalance, "system", "system");

        // Assert
        verify(shiftReportRepository, times(1)).updateOpeningBalance(eq(id), eq(openingBalance), any(Date.class), any(Time.class), eq("system"), eq("system"));
    }

    @Test
    void updateOpeningBalance_whenDataAccessResourceFailureException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        Float openingBalance = 500.0f;
        doThrow(new DataAccessResourceFailureException("Data access failure"))
                .when(shiftReportRepository).updateOpeningBalance(any(UUID.class), anyFloat(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(DataAccessResourceFailureException.class, () -> shiftReportService.updateOpeningBalance(id, openingBalance, "system", "system"));
    }

    @Test
    void updateOpeningBalance_whenJDBCConnectionException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        Float openingBalance = 500.0f;
        doThrow(new JDBCConnectionException("JDBC connection failure", null))
                .when(shiftReportRepository).updateOpeningBalance(any(UUID.class), anyFloat(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(JDBCConnectionException.class, () -> shiftReportService.updateOpeningBalance(id, openingBalance, "system", "system"));
    }

    @Test
    void updateOpeningBalance_whenJpaSystemException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        Float openingBalance = 500.0f;
        doThrow(new JpaSystemException(new RuntimeException("JPA system failure")))
                .when(shiftReportRepository).updateOpeningBalance(any(UUID.class), anyFloat(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(JpaSystemException.class, () -> shiftReportService.updateOpeningBalance(id, openingBalance, "system", "system"));
    }

    @Test
    void updateOpeningBalance_whenTransactionException_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        Float openingBalance = 500.0f;
        doThrow(new TransactionException("Transaction failure"))
                .when(shiftReportRepository).updateOpeningBalance(any(UUID.class), anyFloat(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        assertThrows(TransactionException.class, () -> shiftReportService.updateOpeningBalance(id, openingBalance, "system", "system"));
    }

    @Test
    void updateOpeningBalance_whenUnexpectedException_throwsPersistenceException() {
        // Arrange
        UUID id = UUID.randomUUID();
        Float openingBalance = 500.0f;
        doThrow(new RuntimeException("Unexpected error"))
                .when(shiftReportRepository).updateOpeningBalance(any(UUID.class), anyFloat(), any(Date.class), any(Time.class), anyString(), anyString());

        // Act & Assert
        PersistenceException exception = assertThrows(PersistenceException.class, () -> shiftReportService.updateOpeningBalance(id, openingBalance, "system", "system"));
        assertEquals(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
    }

    // Test case for fetchAllShiftReports method
    @Test
    void fetchAllShiftReports_whenValidPageable_returnsShiftReports() {
        // Arrange
        PageRequest pageable = PageRequest.of(0, 10);
        List<ShiftReport> shiftReportsList = Arrays.asList(createSampleShiftReport(), createSampleShiftReport());
        Page<ShiftReport> shiftReportsPage = new PageImpl<>(shiftReportsList, pageable, shiftReportsList.size());
        when(shiftReportRepository.findAll(any(Pageable.class))).thenReturn(shiftReportsPage);

        // Act
        Page<ShiftReport> result = shiftReportService.fetchAllShiftReports(pageable);

        // Assert
        assertEquals(shiftReportsPage.getTotalElements(), result.getTotalElements());
        verify(shiftReportRepository, times(1)).findAll(eq(pageable));
    }

    @Test
    void fetchAllShiftReports_whenDataAccessResourceFailureException_throwsException() {
        // Arrange
        PageRequest pageable = PageRequest.of(0, 10);
        doThrow(new DataAccessResourceFailureException("Data access failure"))
                .when(shiftReportRepository).findAll(any(Pageable.class));

        // Act & Assert
        assertThrows(DataAccessResourceFailureException.class, () -> shiftReportService.fetchAllShiftReports(pageable));
    }

    @Test
    void fetchAllShiftReports_whenJDBCConnectionException_throwsException() {
        // Arrange
        PageRequest pageable = PageRequest.of(0, 10);
        doThrow(new JDBCConnectionException("JDBC connection failure", null))
                .when(shiftReportRepository).findAll(any(Pageable.class));

        // Act & Assert
        assertThrows(JDBCConnectionException.class, () -> shiftReportService.fetchAllShiftReports(pageable));
    }

    @Test
    void fetchAllShiftReports_whenJpaSystemException_throwsException() {
        // Arrange
        PageRequest pageable = PageRequest.of(0, 10);
        doThrow(new JpaSystemException(new RuntimeException("JPA system failure")))
                .when(shiftReportRepository).findAll(any(Pageable.class));

        // Act & Assert
        assertThrows(JpaSystemException.class, () -> shiftReportService.fetchAllShiftReports(pageable));
    }

    @Test
    void fetchAllShiftReports_whenTransactionException_throwsException() {
        // Arrange
        PageRequest pageable = PageRequest.of(0, 10);
        doThrow(new TransactionException("Transaction failure"))
                .when(shiftReportRepository).findAll(any(Pageable.class));

        // Act & Assert
        assertThrows(TransactionException.class, () -> shiftReportService.fetchAllShiftReports(pageable));
    }

    @Test
    void fetchAllShiftReports_whenUnexpectedException_throwsPersistenceException() {
        // Arrange
        PageRequest pageable = PageRequest.of(0, 10);
        doThrow(new RuntimeException("Unexpected error"))
                .when(shiftReportRepository).findAll(any(Pageable.class));

        // Act & Assert
        PersistenceException exception = assertThrows(PersistenceException.class, () -> shiftReportService.fetchAllShiftReports(pageable));
        assertEquals(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
    }

    // Helper method to create a sample ShiftReport object
    private ShiftReport createSampleShiftReport() {
        ShiftReport shiftReport = new ShiftReport();
        shiftReport.setClosingShiftDate(new Date(System.currentTimeMillis()));
        shiftReport.setClosingShiftTime(new Time(System.currentTimeMillis()));
        shiftReport.setAttendantName("John Doe");
        shiftReport.setReconcilorName("Jane Smith");
        shiftReport.setReconcilorSign("Signature");
        shiftReport.setAttendantSign("Signature");
        shiftReport.setRevenueInCard(100.0f);
        shiftReport.setRevenueInCash(200.0f);
        shiftReport.setShiftTotal("300.0");
        shiftReport.setOpeningBalance(50.0f);
        shiftReport.setCreatedTime(new Time(System.currentTimeMillis()));
        shiftReport.setCreatedDate(new Date(System.currentTimeMillis()));
        shiftReport.setLastUpdatedDate(new Date(System.currentTimeMillis()));
        shiftReport.setLastUpdatedTime(new Time(System.currentTimeMillis()));
        shiftReport.setLastUpdatedBy("system");
        shiftReport.setAccessedBy("system");
        return shiftReport;
    }
}
