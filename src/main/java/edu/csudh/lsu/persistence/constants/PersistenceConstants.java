package edu.csudh.lsu.persistence.constants;

/**
 * This class is used to maintain the constants for persistence
 */
public class PersistenceConstants {

    public static final String DEFINE_THE_ENTITY_WITH_PROPER_JPA_ANNOTATIONS = "Define the entity with proper JPA annotations";

    /**
     * Constructor for {@link PersistenceConstants} which will throw IllegalStateException
     */
    private PersistenceConstants() {
        throw new IllegalStateException("Utility Class");
    }

    // Exception
    public static final String PERSISTENCE_EXCEPTION = "PERSISTENCE EXCEPTION";

    // Error Messages
    public static final String ENTITY_MUST_NOT_BE_EMPTY = "Entity must not be empty.";
    public static final String ENTITY_MUST_NOT_BE_NULL = "Entity must not be null.";
    public static final String AN_EXCEPTION_OCCURRED_WHILE_UPSERTING_A_RECORD = "An exception occurred while upserting a record.";

    // Format
    public static final String DATE = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    public static final String COMMA = ",";
    public static final String OPEN_BRACKET = ")";
    public static final String CLOSE_BRACKET = "(";


}
