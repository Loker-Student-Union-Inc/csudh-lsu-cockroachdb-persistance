package edu.csudh.lsu.persistence.utils;

import lombok.extern.slf4j.Slf4j;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;

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
     * Method to get current time in UTC timezone format.
     *
     * @return Timestamp
     */
    public static Timestamp getFormattedCurrentUTCTime() {
        log.debug("Fetching current time in UTC format.");
        return new Timestamp(Instant.now().toEpochMilli());
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

    /**
     * Adds a specified number of days to the given date.
     *
     * @param date Date to which days will be added.
     * @param days Number of days to add.
     * @return Date with the specified number of days added.
     */
    public static Date addDate(Date date, int days) {
        log.debug("Adding {} days to date: {}", days, date);
        var calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return new Date(calendar.getTimeInMillis());
    }

    /**
     * Converts a UTC timestamp to PST time.
     *
     * @param utcTimestamp Timestamp in UTC to be converted.
     * @return Converted Time in PST.
     */
    public static Time convertUTCToPST(Timestamp utcTimestamp) {
        log.debug("Converting UTC timestamp {} to PST time.", utcTimestamp);
        ZonedDateTime utcZonedDateTime = utcTimestamp.toInstant().atZone(ZoneId.of(UTC_TIMEZONE));
        ZonedDateTime pstZonedDateTime = utcZonedDateTime.withZoneSameInstant(ZoneId.of(PST_TIMEZONE));
        return Time.valueOf(pstZonedDateTime.toLocalTime());
    }

    /**
     * Formats a given date to a specified pattern.
     *
     * @param date    Date to format.
     * @param pattern Pattern to format the date.
     * @return Formatted date as a String.
     */
    public static String formatDate(Date date, String pattern) {
        log.debug("Formatting date {} with pattern '{}'.", date, pattern);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }
}
