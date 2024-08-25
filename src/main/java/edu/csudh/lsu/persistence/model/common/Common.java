package edu.csudh.lsu.persistence.model.common;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Date;
import java.sql.Time;

@MappedSuperclass
@NoArgsConstructor
public class Common {

    @Getter
    @Setter
    private Time createdTime;       // Time when the record was created

    @Getter
    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdDate;       // Date and time when the record was created

    @Getter
    @Setter
    private Date lastUpdatedDate;

    @Getter
    @Setter
    private Time lastUpdatedTime;

    @Getter
    @Setter
    private String lastUpdatedBy;   // Tracks the user who last updated the record

    @Getter
    @Setter
    private String accessedBy;      // Tracks the user who last accessed or interacted with the record
}
