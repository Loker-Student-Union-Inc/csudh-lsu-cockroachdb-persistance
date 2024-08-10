package edu.csudh.lsu.persistence.utils;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TimeUtilsTest {

    @Test
    void testUtilityClassConstructor() {
        // Ensure the utility class constructor throws an exception
        assertThrows(IllegalStateException.class, TimeUtils::new, "Expected constructor to throw, but it didn't");
    }

    @Test
    void testGetFormattedCurrentPSTTime() {
        // Fetch the current PST time using the method
        Timestamp currentPSTTime = TimeUtils.getFormattedCurrentPSTTime();

        // Verify that the timestamp is not null
        assertNotNull(currentPSTTime, "Timestamp should not be null");

        // Fetch the current PST time manually
        ZonedDateTime expectedPSTTime = ZonedDateTime.now(ZoneId.of("America/Los_Angeles"));
        Timestamp expectedTimestamp = Timestamp.from(expectedPSTTime.toInstant());

        // Check if the two timestamps are close enough (considering the slight delay)
        assertEquals(expectedTimestamp.toInstant().getEpochSecond(), currentPSTTime.toInstant().getEpochSecond(),
                "The method should return the current time in PST timezone");
    }
}
