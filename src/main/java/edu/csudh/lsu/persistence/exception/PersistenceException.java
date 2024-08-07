package edu.csudh.lsu.persistence.exception;

/**
 * This class is used to handle the custom exception for Persistence Library
 */
public class PersistenceException extends RuntimeException{

    private final String errorDescription;
    private String errorCode;

    /**
     * Constructor to handle the error message initialization
     *
     * @param errorDescription Error Description
     */
    public PersistenceException(String errorDescription) {
        super(errorDescription);
        this.errorDescription = errorDescription;
    }

    /**
     * Constructor to handle the error message  and error code initialization
     *
     * @param errorDescription Error Description
     * @param errorCode Error Code
     */
    public PersistenceException(String errorDescription, String errorCode) {
        super(errorDescription);
        this.errorDescription = errorDescription;
        this.errorCode = errorCode;
    }
}
