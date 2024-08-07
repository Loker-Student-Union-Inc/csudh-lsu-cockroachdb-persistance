package edu.csudh.lsu.persistence.utils;

import lombok.extern.slf4j.Slf4j;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
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

    private TimeUtils () {
        throw new IllegalStateException("Utility Class");
    }

    /**
     * Method to get current time in UTC timezone format
     *
     * @return Timestamp
     */
    public static Timestamp getFormattedCurrentUTCTime() {
        return new Timestamp(Instant.now().toEpochMilli());
    }

    public static Date addDate (Date date, int days) {
        var calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return new Date(calendar.getTimeInMillis());
    }
}
