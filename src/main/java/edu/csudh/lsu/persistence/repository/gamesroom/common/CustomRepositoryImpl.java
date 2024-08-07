package edu.csudh.lsu.persistence.repository.gamesroom.common;

import jakarta.persistence.*;
import lombok.SneakyThrows;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static edu.csudh.lsu.persistence.constants.PersistenceConstants.*;

/**
 * Custom repository implementation for handling UPSERT operations.
 *
 * @param <T> Entity type
 * @param <I> ID type
 */
public class CustomRepositoryImpl<T, I> extends SimpleJpaRepository<T, I> implements CustomRepository<T, I> {

    private JpaEntityInformation<T, ?> entityInformation;
    private EntityManager entityManager;

    /**
     * Constructor for CustomRepositoryImpl.
     *
     * @param entityInformation Entity information
     * @param entityManager Entity manager
     */
    public CustomRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityInformation = entityInformation;
        this.entityManager = entityManager;
    }

    /**
     * Collects all fields annotated with @Column, @JoinColumn, or @EmbeddedId.
     *
     * @param clazz Class to inspect
     * @return List of fields with relevant annotations
     */
    private List<Field> getColumnFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields.stream().filter(field -> field.isAnnotationPresent(Column.class)
                || field.isAnnotationPresent(JoinColumn.class)
                || field.isAnnotationPresent(EmbeddedId.class)).collect(Collectors.toList());
    }

    /**
     * Generates a comma-separated list of column names.
     *
     * @param columnFields List of fields
     * @return Comma-separated column names
     */
    private String populateTableColumns(List<Field> columnFields) {
        var queryBuilder = new StringBuilder();
        for (var field : columnFields) {
            String columnName;
            if (field.isAnnotationPresent(Column.class))
                columnName = field.getAnnotation(Column.class).name();
            else if (field.isAnnotationPresent(JoinColumn.class))
                columnName = field.getAnnotation(JoinColumn.class).name();
            else {
                queryBuilder.append(populateTableColumns(getColumnFields(field.getType()))).append(COMMA);
                continue;
            }
            queryBuilder.append(columnName).append(COMMA);
        }
        queryBuilder.deleteCharAt(queryBuilder.lastIndexOf(COMMA));
        return queryBuilder.toString();
    }

    /**
     * Constructs the initial part of the UPSERT SQL query.
     *
     * @param columnFields List of fields
     * @param tableName Name of the table
     * @return Initial part of the UPSERT query
     */
    private String formInitialQuery(List<Field> columnFields, String tableName) {
        return "UPSERT INTO " +
                tableName +
                OPEN_BRACKET +
                populateTableColumns(columnFields) +
                CLOSE_BRACKET +
                " VALUES ";
    }

    /**
     * Generates parameter placeholders for the SQL query.
     *
     * @param columnFields List of fields
     * @param index Index for parameter placeholders
     * @return Comma-separated parameter placeholders
     */
    private String populateColumnParams(List<Field> columnFields, int index) {
        var valuesBuilder = new StringBuilder();
        for (var field : columnFields) {
            if (field.isAnnotationPresent(EmbeddedId.class))
                valuesBuilder.append(populateColumnParams(getColumnFields(field.getType()), index)).append(COMMA);
            else {
                valuesBuilder.append(":");
                valuesBuilder.append(field.getName().trim()).append(index);
                valuesBuilder.append(COMMA);
            }
        }
        valuesBuilder.deleteCharAt(valuesBuilder.lastIndexOf(COMMA));
        return valuesBuilder.toString();
    }

    /**
     * Sets the values of the parameters in the query from the entity's fields.
     *
     * @param columnFields List of fields
     * @param entity Entity to extract values from
     * @param nativeQuery Query to set parameters on
     * @param index Index for parameter placeholders
     * @param <K> Type of the entity
     * @throws PersistenceException if an error occurs while accessing entity properties
     */
    @SneakyThrows
    private <K> void populateColumnValues(List<Field> columnFields, K entity, Query nativeQuery, int index) {
        for (var field : columnFields) {
            try {
                var pd = new PropertyDescriptor(field.getName(), entity.getClass());
                if (field.isAnnotationPresent(EmbeddedId.class)) {
                    final I primaryKey = (I) entityInformation.getId((T) entity);
                    populateColumnValues(getColumnFields(field.getType()), primaryKey, nativeQuery, index);
                }
                else
                    nativeQuery.setParameter(field.getName() + index, pd.getReadMethod().invoke(entity));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new PersistenceException(AN_EXCEPTION_OCCURRED_WHILE_UPSERTING_A_RECORD);
            }
        }
    }

    /**
     * Performs the upsert operation on the list of entities.
     *
     * @param entities List of entities to upsert
     * @return List of upserted entities
     * @throws PersistenceException if an error occurs during the upsert operation
     */
    @Override
    @SneakyThrows
    public List<T> upsertAll(List<T> entities) {

        Assert.notNull(entities, ENTITY_MUST_NOT_BE_NULL); // Ensure entities list is not null
        Assert.notEmpty(entities, ENTITY_MUST_NOT_BE_EMPTY); // Ensure entities list is not empty

        final T sampleEntity = entities.get(0); // Get the first entity as a sample
        final List<Field> columnFields = getColumnFields(sampleEntity.getClass()); // Get column fields of the sample entity

        Assert.notEmpty(columnFields, DEFINE_THE_ENTITY_WITH_PROPER_JPA_ANNOTATIONS); // Ensure there are column fields

        var queryBuilder = new StringBuilder();
        queryBuilder.append(formInitialQuery(columnFields, sampleEntity.getClass().getAnnotation(Table.class).name())); // Form initial UPSERT query
        var index = new AtomicInteger(0); // Initialize index
        entities.forEach(entity -> queryBuilder.append(OPEN_BRACKET).append(populateColumnParams(columnFields, index.getAndIncrement()))
                .append(CLOSE_BRACKET).append(COMMA)); // Append parameter placeholders
        queryBuilder.deleteCharAt(queryBuilder.lastIndexOf(COMMA)); // Remove trailing comma
        final var nativeQuery = entityManager.createNativeQuery(queryBuilder.toString(), sampleEntity.getClass()); // Create native query
        index.set(0); // Reset index
        entities.forEach(entity -> populateColumnValues(columnFields, entity, nativeQuery, index.getAndIncrement())); // Set parameter values
        nativeQuery.executeUpdate(); // Execute the query
        return entities;
    }
}
