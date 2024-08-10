package edu.csudh.lsu.persistence.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersistenceExceptionTest {

    @Test
    void testPersistenceExceptionWithDescriptionOnly() {
        String errorDescription = "This is an error description";

        PersistenceException exception = new PersistenceException(errorDescription);

        assertEquals(errorDescription, exception.getMessage());
    }

    @Test
    void testPersistenceExceptionWithDescriptionAndCode() {
        String errorDescription = "This is another error description";
        String errorCode = "ERR_001";

        PersistenceException exception = new PersistenceException(errorDescription, errorCode);

        assertEquals(errorDescription, exception.getMessage());
    }
}
