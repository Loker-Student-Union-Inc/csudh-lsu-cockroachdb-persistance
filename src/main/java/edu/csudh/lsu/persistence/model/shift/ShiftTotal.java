package edu.csudh.lsu.persistence.model.shift;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.csudh.lsu.persistence.model.View;
import edu.csudh.lsu.persistence.model.common.Common;
import lombok.ToString;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@ToString
@Entity
@Table(name = "SHIFT_TOTAL")
@JsonDeserialize
@JsonSerialize
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class ShiftTotal extends Common {

    @Getter
    @Setter
    @JsonView(View.Json.class)
    @Column(name = "ID", nullable = false)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Id
    private UUID id;

    @Getter
    @Setter
    @JsonView(View.Json.class)
    @Column(name = "STUDENT_NAME", nullable = false)
    private String studentName;

    @Getter
    @Setter
    @JsonView(View.Json.class)
    @Column(name = "ATTENDANT_NAME", nullable = false)
    private String attendantName;

    @Getter
    @Setter
    @JsonView(View.Json.class)
    @Column(name = "ACTIVITY", nullable = false)
    private String activity;

    @Getter
    @Setter
    @JsonView(View.Json.class)
    @Column(name = "COST", nullable = false)
    private Float cost;

    @Getter
    @Setter
    @JsonView(View.Json.class)
    @Column(name = "PAYMENT_MODE", nullable = false)
    private String paymentMode;

    @Getter
    @JsonView(View.Json.class)
    @Column(name = "START_TIME", nullable = false)
    private Time startTime;

    @Getter
    @JsonView(View.Json.class)
    @Column(name = "DATE", nullable = false)
    private Date date;

    @Getter
    @Setter
    @JsonView(View.Json.class)
    @Column(name = "DURATION", nullable = false)
    private String duration;

    @PrePersist
    private void onCreate() {
        this.startTime = Time.valueOf(LocalTime.now());
        this.date = Date.valueOf(LocalDate.now());
    }
}
