package edu.csudh.lsu.persistence.utils;

import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * <p>
 * Class to handle time conversion or manipulation methods.
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

@Slf4j
public class TimeUtils {

    private static final String PST_TIMEZONE = "America/Los_Angeles";
    private static final String UTC_TIMEZONE = "UTC";

    private TimeUtils() {
        throw new IllegalStateException("Utility Class");
    }

    /**
     * Method to get current time in PST timezone format.
     *
     * @return Timestamp
     */
    public static Timestamp getFormattedCurrentPSTTime() {
        log.debug("Fetching current time in PST format.");
        ZonedDateTime pstTime = ZonedDateTime.now(ZoneId.of(PST_TIMEZONE));
        return Timestamp.from(pstTime.toInstant());
    }

}
