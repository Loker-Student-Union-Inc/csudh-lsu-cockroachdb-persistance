package edu.csudh.lsu.persistence.utils;

import java.util.Objects;

/**
 * <p>
 * PersistenceStringUtils to have common String Utility methods.
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
public class PersistenceStringUtils {

    private PersistenceStringUtils() {
    }

    public static boolean isNotNullOrEmpty(String value) {
        return Objects.nonNull(value) && !value.isEmpty();
    }
}
