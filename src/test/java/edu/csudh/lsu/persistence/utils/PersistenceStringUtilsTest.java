package edu.csudh.lsu.persistence.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PersistenceStringUtilsTest {

    @Test
    void testIsNotNullOrEmpty() {
        // Test with null input
        assertFalse(PersistenceStringUtils.isNotNullOrEmpty(null), "Should return false for null input");

        // Test with empty string input
        assertFalse(PersistenceStringUtils.isNotNullOrEmpty(""), "Should return false for empty string input");

        // Test with a string containing only whitespace
        assertTrue(PersistenceStringUtils.isNotNullOrEmpty("   "), "Should return true for whitespace string");

        // Test with a non-empty string
        assertTrue(PersistenceStringUtils.isNotNullOrEmpty("hello"), "Should return true for non-empty string");

        // Test with a string containing only newline character
        assertTrue(PersistenceStringUtils.isNotNullOrEmpty("\n"), "Should return true for newline character string");

        // Test with a string containing spaces and characters
        assertTrue(PersistenceStringUtils.isNotNullOrEmpty("text with spaces"), "Should return true for string with spaces and characters");

        // Test with a string containing only a tab character
        assertTrue(PersistenceStringUtils.isNotNullOrEmpty("\t"), "Should return true for tab character string");

        // Test with a typical non-empty string
        assertTrue(PersistenceStringUtils.isNotNullOrEmpty("non-null string"), "Should return true for typical non-empty string");
    }
}
