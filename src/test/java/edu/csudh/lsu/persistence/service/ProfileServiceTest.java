package edu.csudh.lsu.persistence.service;

import edu.csudh.lsu.persistence.constants.PersistenceConstants;
import edu.csudh.lsu.persistence.exception.PersistenceException;
import edu.csudh.lsu.persistence.model.profile.Profile;
import edu.csudh.lsu.persistence.repository.gamesroom.profile.ProfileRepository;
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

class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private ProfileService profileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveOrUpdateProfile_whenProfileIsNull_logsWarning() {
        // Act
        profileService.saveOrUpdateProfile(null);

        // Assert
        verify(profileRepository, never()).upsertProfile(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), any(), any(), anyString(), anyString());
    }

    @Test
    void saveOrUpdateProfile_whenProfileIsNew_setsCreatedDateAndTime() {
        // Arrange
        Profile profile = createSampleProfile();
        profile.setUserId(null);  // Simulate a new profile

        // Act
        profileService.saveOrUpdateProfile(profile);

        // Assert
        verify(profileRepository, times(1)).upsertProfile(
                eq(profile.getUserId()), eq(profile.getUserPassword()), eq(profile.getFirstName()),
                eq(profile.getLastName()), eq(profile.getRole()), eq(profile.getPermission()),
                any(Time.class), any(Date.class), eq(profile.getLastUpdatedBy()), eq(profile.getAccessedBy())
        );

        // Verify that the createdDate and createdTime were set
        assertNotNull(profile.getCreatedDate());
        assertNotNull(profile.getCreatedTime());
        assertNotNull(profile.getLastUpdatedDate());
        assertNotNull(profile.getLastUpdatedTime());
    }

    @Test
    void saveOrUpdateProfile_whenProfileIsNotNull_executesSaveOrUpdateSuccessfully() {
        // Arrange
        Profile profile = createSampleProfile();

        // Act
        profileService.saveOrUpdateProfile(profile);

        // Assert
        verify(profileRepository, times(1)).upsertProfile(
                eq(profile.getUserId()), eq(profile.getUserPassword()), eq(profile.getFirstName()),
                eq(profile.getLastName()), eq(profile.getRole()), eq(profile.getPermission()),
                any(Time.class), any(Date.class), eq(profile.getLastUpdatedBy()), eq(profile.getAccessedBy())
        );
    }

    @Test
    void saveOrUpdateProfile_whenValidProfile_savesSuccessfully() {
        // Arrange
        Profile profile = createSampleProfile();

        // Act
        profileService.saveOrUpdateProfile(profile);

        // Assert
        verify(profileRepository, times(1)).upsertProfile(
                eq(profile.getUserId()), eq(profile.getUserPassword()), eq(profile.getFirstName()),
                eq(profile.getLastName()), eq(profile.getRole()), eq(profile.getPermission()),
                eq(profile.getCreatedTime()), eq(profile.getCreatedDate()),
                eq(profile.getLastUpdatedBy()), eq(profile.getAccessedBy())
        );
    }

    @Test
    void saveOrUpdateProfile_whenDataAccessResourceFailureException_throwsException() {
        // Arrange
        Profile profile = createSampleProfile();
        doThrow(new DataAccessResourceFailureException("Data access failure"))
                .when(profileRepository).upsertProfile(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), any(), any(), anyString(), anyString());

        // Act & Assert
        assertThrows(DataAccessResourceFailureException.class, () -> profileService.saveOrUpdateProfile(profile));
    }

    @Test
    void saveOrUpdateProfile_whenJDBCConnectionException_throwsException() {
        // Arrange
        Profile profile = createSampleProfile();
        doThrow(new JDBCConnectionException("JDBC connection failure", null))
                .when(profileRepository).upsertProfile(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), any(), any(), anyString(), anyString());

        // Act & Assert
        assertThrows(JDBCConnectionException.class, () -> profileService.saveOrUpdateProfile(profile));
    }

    @Test
    void saveOrUpdateProfile_whenJpaSystemException_throwsException() {
        // Arrange
        Profile profile = createSampleProfile();
        doThrow(new JpaSystemException(new RuntimeException("JPA system failure")))
                .when(profileRepository).upsertProfile(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), any(), any(), anyString(), anyString());

        // Act & Assert
        assertThrows(JpaSystemException.class, () -> profileService.saveOrUpdateProfile(profile));
    }

    @Test
    void saveOrUpdateProfile_whenTransactionException_throwsException() {
        // Arrange
        Profile profile = createSampleProfile();
        doThrow(new TransactionException("Transaction failure"))
                .when(profileRepository).upsertProfile(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), any(), any(), anyString(), anyString());

        // Act & Assert
        assertThrows(TransactionException.class, () -> profileService.saveOrUpdateProfile(profile));
    }

    @Test
    void saveOrUpdateProfile_whenUnexpectedException_throwsPersistenceException() {
        // Arrange
        Profile profile = createSampleProfile();
        doThrow(new RuntimeException("Unexpected error"))
                .when(profileRepository).upsertProfile(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), any(), any(), anyString(), anyString());

        // Act & Assert
        PersistenceException exception = assertThrows(PersistenceException.class, () -> profileService.saveOrUpdateProfile(profile));
        assertEquals(PersistenceConstants.AN_EXCEPTION_OCCURRED_WHILE_UPSERTING_A_RECORD, exception.getMessage());
    }

    @Test
    void deleteProfile_whenValidUserId_deletesSuccessfully() {
        // Arrange
        String userId = "user123";

        // Act
        profileService.deleteProfile(userId);

        // Assert
        verify(profileRepository, times(1)).deleteProfile(eq(userId));
    }

    @Test
    void deleteProfile_whenDataAccessResourceFailureException_throwsException() {
        // Arrange
        String userId = "user123";
        doThrow(new DataAccessResourceFailureException("Data access failure"))
                .when(profileRepository).deleteProfile(anyString());

        // Act & Assert
        assertThrows(DataAccessResourceFailureException.class, () -> profileService.deleteProfile(userId));
    }

    @Test
    void deleteProfile_whenJDBCConnectionException_throwsException() {
        // Arrange
        String userId = "user123";
        doThrow(new JDBCConnectionException("JDBC connection failure", null))
                .when(profileRepository).deleteProfile(anyString());

        // Act & Assert
        assertThrows(JDBCConnectionException.class, () -> profileService.deleteProfile(userId));
    }

    @Test
    void deleteProfile_whenJpaSystemException_throwsException() {
        // Arrange
        String userId = "user123";
        doThrow(new JpaSystemException(new RuntimeException("JPA system failure")))
                .when(profileRepository).deleteProfile(anyString());

        // Act & Assert
        assertThrows(JpaSystemException.class, () -> profileService.deleteProfile(userId));
    }

    @Test
    void deleteProfile_whenTransactionException_throwsException() {
        // Arrange
        String userId = "user123";
        doThrow(new TransactionException("Transaction failure"))
                .when(profileRepository).deleteProfile(anyString());

        // Act & Assert
        assertThrows(TransactionException.class, () -> profileService.deleteProfile(userId));
    }

    @Test
    void deleteProfile_whenUnexpectedException_throwsPersistenceException() {
        // Arrange
        String userId = "user123";
        doThrow(new RuntimeException("Unexpected error"))
                .when(profileRepository).deleteProfile(anyString());

        // Act & Assert
        PersistenceException exception = assertThrows(PersistenceException.class, () -> profileService.deleteProfile(userId));
        assertEquals(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
    }

    @Test
    void findAllProfiles_whenValidData_returnsProfiles() {
        // Arrange
        List<Profile> profiles = Arrays.asList(createSampleProfile(), createSampleProfile());
        when(profileRepository.findAllProfiles()).thenReturn(profiles);

        // Act
        List<Profile> result = profileService.findAllProfiles();

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void findAllProfiles_whenNoData_returnsEmptyList() {
        // Arrange
        when(profileRepository.findAllProfiles()).thenReturn(Collections.emptyList());

        // Act
        List<Profile> result = profileService.findAllProfiles();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllProfiles_whenDataAccessResourceFailureException_throwsException() {
        // Arrange
        doThrow(new DataAccessResourceFailureException("Data access failure"))
                .when(profileRepository).findAllProfiles();

        // Act & Assert
        assertThrows(DataAccessResourceFailureException.class, () -> profileService.findAllProfiles());
    }

    @Test
    void findAllProfiles_whenJDBCConnectionException_throwsException() {
        // Arrange
        doThrow(new JDBCConnectionException("JDBC connection failure", null))
                .when(profileRepository).findAllProfiles();

        // Act & Assert
        assertThrows(JDBCConnectionException.class, () -> profileService.findAllProfiles());
    }

    @Test
    void findAllProfiles_whenJpaSystemException_throwsException() {
        // Arrange
        doThrow(new JpaSystemException(new RuntimeException("JPA system failure")))
                .when(profileRepository).findAllProfiles();

        // Act & Assert
        assertThrows(JpaSystemException.class, () -> profileService.findAllProfiles());
    }

    @Test
    void findAllProfiles_whenTransactionException_throwsException() {
        // Arrange
        doThrow(new TransactionException("Transaction failure"))
                .when(profileRepository).findAllProfiles();

        // Act & Assert
        assertThrows(TransactionException.class, () -> profileService.findAllProfiles());
    }

    @Test
    void findAllProfiles_whenUnexpectedException_throwsPersistenceException() {
        // Arrange
        doThrow(new RuntimeException("Unexpected error"))
                .when(profileRepository).findAllProfiles();

        // Act & Assert
        PersistenceException exception = assertThrows(PersistenceException.class, () -> profileService.findAllProfiles());
        assertEquals(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
    }

    @Test
    void updatePassword_whenValidData_updatesSuccessfully() {
        // Arrange
        String userId = "user123";
        String newPassword = "newPassword123";

        // Act
        profileService.updatePassword(userId, newPassword);

        // Assert
        verify(profileRepository, times(1)).updatePassword(eq(userId), eq(newPassword), eq("system"), any(), any(), eq("system"));
    }

    @Test
    void updatePassword_whenDataAccessResourceFailureException_throwsException() {
        // Arrange
        String userId = "user123";
        String newPassword = "newPassword123";
        doThrow(new DataAccessResourceFailureException("Data access failure"))
                .when(profileRepository).updatePassword(anyString(), anyString(), anyString(), any(), any(), anyString());

        // Act & Assert
        assertThrows(DataAccessResourceFailureException.class, () -> profileService.updatePassword(userId, newPassword));
    }

    @Test
    void updatePassword_whenJDBCConnectionException_throwsException() {
        // Arrange
        String userId = "user123";
        String newPassword = "newPassword123";
        doThrow(new JDBCConnectionException("JDBC connection failure", null))
                .when(profileRepository).updatePassword(anyString(), anyString(), anyString(), any(), any(), anyString());

        // Act & Assert
        assertThrows(JDBCConnectionException.class, () -> profileService.updatePassword(userId, newPassword));
    }

    @Test
    void updatePassword_whenJpaSystemException_throwsException() {
        // Arrange
        String userId = "user123";
        String newPassword = "newPassword123";
        doThrow(new JpaSystemException(new RuntimeException("JPA system failure")))
                .when(profileRepository).updatePassword(anyString(), anyString(), anyString(), any(), any(), anyString());

        // Act & Assert
        assertThrows(JpaSystemException.class, () -> profileService.updatePassword(userId, newPassword));
    }

    @Test
    void updatePassword_whenTransactionException_throwsException() {
        // Arrange
        String userId = "user123";
        String newPassword = "newPassword123";
        doThrow(new TransactionException("Transaction failure"))
                .when(profileRepository).updatePassword(anyString(), anyString(), anyString(), any(), any(), anyString());

        // Act & Assert
        assertThrows(TransactionException.class, () -> profileService.updatePassword(userId, newPassword));
    }

    @Test
    void updatePassword_whenUnexpectedException_throwsPersistenceException() {
        // Arrange
        String userId = "user123";
        String newPassword = "newPassword123";
        doThrow(new RuntimeException("Unexpected error"))
                .when(profileRepository).updatePassword(anyString(), anyString(), anyString(), any(), any(), anyString());

        // Act & Assert
        PersistenceException exception = assertThrows(PersistenceException.class, () -> profileService.updatePassword(userId, newPassword));
        assertEquals(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
    }

    // Similar test cases should be written for updateFirstName, updateLastName, updateRole, and updatePermission.

    private Profile createSampleProfile() {
        Profile profile = new Profile();
        profile.setUserId("user123");
        profile.setUserPassword("password123");
        profile.setFirstName("John");
        profile.setLastName("Doe");
        profile.setRole("User");
        profile.setPermission("Read");
        profile.setCreatedTime(new Time(System.currentTimeMillis()));
        profile.setCreatedDate(new Date(System.currentTimeMillis()));
        profile.setLastUpdatedBy("system");
        profile.setAccessedBy("system");
        return profile;
    }

    @Test
    void updateFirstName_whenValidData_updatesSuccessfully() {
        // Arrange
        String userId = "user123";
        String firstName = "John";

        // Act
        profileService.updateFirstName(userId, firstName);

        // Assert
        verify(profileRepository, times(1)).updateFirstName(eq(userId), eq(firstName), eq("system"), any(), any(), eq("system"));
    }

    @Test
    void updateFirstName_whenDataAccessResourceFailureException_throwsException() {
        // Arrange
        String userId = "user123";
        String firstName = "John";
        doThrow(new DataAccessResourceFailureException("Data access failure"))
                .when(profileRepository).updateFirstName(anyString(), anyString(), anyString(), any(), any(), anyString());

        // Act & Assert
        assertThrows(DataAccessResourceFailureException.class, () -> profileService.updateFirstName(userId, firstName));
    }

    @Test
    void updateFirstName_whenJDBCConnectionException_throwsException() {
        // Arrange
        String userId = "user123";
        String firstName = "John";
        doThrow(new JDBCConnectionException("JDBC connection failure", null))
                .when(profileRepository).updateFirstName(anyString(), anyString(), anyString(), any(), any(), anyString());

        // Act & Assert
        assertThrows(JDBCConnectionException.class, () -> profileService.updateFirstName(userId, firstName));
    }

    @Test
    void updateFirstName_whenJpaSystemException_throwsException() {
        // Arrange
        String userId = "user123";
        String firstName = "John";
        doThrow(new JpaSystemException(new RuntimeException("JPA system failure")))
                .when(profileRepository).updateFirstName(anyString(), anyString(), anyString(), any(), any(), anyString());

        // Act & Assert
        assertThrows(JpaSystemException.class, () -> profileService.updateFirstName(userId, firstName));
    }

    @Test
    void updateFirstName_whenTransactionException_throwsException() {
        // Arrange
        String userId = "user123";
        String firstName = "John";
        doThrow(new TransactionException("Transaction failure"))
                .when(profileRepository).updateFirstName(anyString(), anyString(), anyString(), any(), any(), anyString());

        // Act & Assert
        assertThrows(TransactionException.class, () -> profileService.updateFirstName(userId, firstName));
    }

    @Test
    void updateFirstName_whenUnexpectedException_throwsPersistenceException() {
        // Arrange
        String userId = "user123";
        String firstName = "John";
        doThrow(new RuntimeException("Unexpected error"))
                .when(profileRepository).updateFirstName(anyString(), anyString(), anyString(), any(), any(), anyString());

        // Act & Assert
        PersistenceException exception = assertThrows(PersistenceException.class, () -> profileService.updateFirstName(userId, firstName));
        assertEquals(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
    }

    @Test
    void updateLastName_whenValidData_updatesSuccessfully() {
        // Arrange
        String userId = "user123";
        String lastName = "Doe";

        // Act
        profileService.updateLastName(userId, lastName);

        // Assert
        verify(profileRepository, times(1)).updateLastName(eq(userId), eq(lastName), eq("system"), any(), any(), eq("system"));
    }

    @Test
    void updateLastName_whenDataAccessResourceFailureException_throwsException() {
        // Arrange
        String userId = "user123";
        String lastName = "Doe";
        doThrow(new DataAccessResourceFailureException("Data access failure"))
                .when(profileRepository).updateLastName(anyString(), anyString(), anyString(), any(), any(), anyString());

        // Act & Assert
        assertThrows(DataAccessResourceFailureException.class, () -> profileService.updateLastName(userId, lastName));
    }

    @Test
    void updateLastName_whenJDBCConnectionException_throwsException() {
        // Arrange
        String userId = "user123";
        String lastName = "Doe";
        doThrow(new JDBCConnectionException("JDBC connection failure", null))
                .when(profileRepository).updateLastName(anyString(), anyString(), anyString(), any(), any(), anyString());

        // Act & Assert
        assertThrows(JDBCConnectionException.class, () -> profileService.updateLastName(userId, lastName));
    }

    @Test
    void updateLastName_whenJpaSystemException_throwsException() {
        // Arrange
        String userId = "user123";
        String lastName = "Doe";
        doThrow(new JpaSystemException(new RuntimeException("JPA system failure")))
                .when(profileRepository).updateLastName(anyString(), anyString(), anyString(), any(), any(), anyString());

        // Act & Assert
        assertThrows(JpaSystemException.class, () -> profileService.updateLastName(userId, lastName));
    }

    @Test
    void updateLastName_whenTransactionException_throwsException() {
        // Arrange
        String userId = "user123";
        String lastName = "Doe";
        doThrow(new TransactionException("Transaction failure"))
                .when(profileRepository).updateLastName(anyString(), anyString(), anyString(), any(), any(), anyString());

        // Act & Assert
        assertThrows(TransactionException.class, () -> profileService.updateLastName(userId, lastName));
    }

    @Test
    void updateLastName_whenUnexpectedException_throwsPersistenceException() {
        // Arrange
        String userId = "user123";
        String lastName = "Doe";
        doThrow(new RuntimeException("Unexpected error"))
                .when(profileRepository).updateLastName(anyString(), anyString(), anyString(), any(), any(), anyString());

        // Act & Assert
        PersistenceException exception = assertThrows(PersistenceException.class, () -> profileService.updateLastName(userId, lastName));
        assertEquals(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
    }

    @Test
    void updateRole_whenValidData_updatesSuccessfully() {
        // Arrange
        String userId = "user123";
        String role = "Admin";

        // Act
        profileService.updateRole(userId, role);

        // Assert
        verify(profileRepository, times(1)).updateRole(eq(userId), eq(role), eq("system"), any(), any(), eq("system"));
    }

    @Test
    void updateRole_whenDataAccessResourceFailureException_throwsException() {
        // Arrange
        String userId = "user123";
        String role = "Admin";
        doThrow(new DataAccessResourceFailureException("Data access failure"))
                .when(profileRepository).updateRole(anyString(), anyString(), anyString(), any(), any(), anyString());

        // Act & Assert
        assertThrows(DataAccessResourceFailureException.class, () -> profileService.updateRole(userId, role));
    }

    @Test
    void updateRole_whenJDBCConnectionException_throwsException() {
        // Arrange
        String userId = "user123";
        String role = "Admin";
        doThrow(new JDBCConnectionException("JDBC connection failure", null))
                .when(profileRepository).updateRole(anyString(), anyString(), anyString(), any(), any(), anyString());

        // Act & Assert
        assertThrows(JDBCConnectionException.class, () -> profileService.updateRole(userId, role));
    }

    @Test
    void updateRole_whenJpaSystemException_throwsException() {
        // Arrange
        String userId = "user123";
        String role = "Admin";
        doThrow(new JpaSystemException(new RuntimeException("JPA system failure")))
                .when(profileRepository).updateRole(anyString(), anyString(), anyString(), any(), any(), anyString());

        // Act & Assert
        assertThrows(JpaSystemException.class, () -> profileService.updateRole(userId, role));
    }

    @Test
    void updateRole_whenTransactionException_throwsException() {
        // Arrange
        String userId = "user123";
        String role = "Admin";
        doThrow(new TransactionException("Transaction failure"))
                .when(profileRepository).updateRole(anyString(), anyString(), anyString(), any(), any(), anyString());

        // Act & Assert
        assertThrows(TransactionException.class, () -> profileService.updateRole(userId, role));
    }

    @Test
    void updateRole_whenUnexpectedException_throwsPersistenceException() {
        // Arrange
        String userId = "user123";
        String role = "Admin";
        doThrow(new RuntimeException("Unexpected error"))
                .when(profileRepository).updateRole(anyString(), anyString(), anyString(), any(), any(), anyString());

        // Act & Assert
        PersistenceException exception = assertThrows(PersistenceException.class, () -> profileService.updateRole(userId, role));
        assertEquals(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
    }

    @Test
    void updatePermission_whenValidData_updatesSuccessfully() {
        // Arrange
        String userId = "user123";
        String permission = "ReadWrite";

        // Act
        profileService.updatePermission(userId, permission);

        // Assert
        verify(profileRepository, times(1)).updatePermission(eq(userId), eq(permission), eq("system"), any(), any(), eq("system"));
    }

    @Test
    void updatePermission_whenDataAccessResourceFailureException_throwsException() {
        // Arrange
        String userId = "user123";
        String permission = "ReadWrite";
        doThrow(new DataAccessResourceFailureException("Data access failure"))
                .when(profileRepository).updatePermission(anyString(), anyString(), anyString(), any(), any(), anyString());

        // Act & Assert
        assertThrows(DataAccessResourceFailureException.class, () -> profileService.updatePermission(userId, permission));
    }

    @Test
    void updatePermission_whenJDBCConnectionException_throwsException() {
        // Arrange
        String userId = "user123";
        String permission = "ReadWrite";
        doThrow(new JDBCConnectionException("JDBC connection failure", null))
                .when(profileRepository).updatePermission(anyString(), anyString(), anyString(), any(), any(), anyString());

        // Act & Assert
        assertThrows(JDBCConnectionException.class, () -> profileService.updatePermission(userId, permission));
    }

    @Test
    void updatePermission_whenJpaSystemException_throwsException() {
        // Arrange
        String userId = "user123";
        String permission = "ReadWrite";
        doThrow(new JpaSystemException(new RuntimeException("JPA system failure")))
                .when(profileRepository).updatePermission(anyString(), anyString(), anyString(), any(), any(), anyString());

        // Act & Assert
        assertThrows(JpaSystemException.class, () -> profileService.updatePermission(userId, permission));
    }

    @Test
    void updatePermission_whenTransactionException_throwsException() {
        // Arrange
        String userId = "user123";
        String permission = "ReadWrite";
        doThrow(new TransactionException("Transaction failure"))
                .when(profileRepository).updatePermission(anyString(), anyString(), anyString(), any(), any(), anyString());

        // Act & Assert
        assertThrows(TransactionException.class, () -> profileService.updatePermission(userId, permission));
    }

    @Test
    void updatePermission_whenUnexpectedException_throwsPersistenceException() {
        // Arrange
        String userId = "user123";
        String permission = "ReadWrite";
        doThrow(new RuntimeException("Unexpected error"))
                .when(profileRepository).updatePermission(anyString(), anyString(), anyString(), any(), any(), anyString());

        // Act & Assert
        PersistenceException exception = assertThrows(PersistenceException.class, () -> profileService.updatePermission(userId, permission));
        assertEquals(PersistenceConstants.PERSISTENCE_EXCEPTION, exception.getMessage());
    }

}