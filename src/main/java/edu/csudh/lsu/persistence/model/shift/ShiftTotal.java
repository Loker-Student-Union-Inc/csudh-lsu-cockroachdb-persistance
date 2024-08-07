package edu.csudh.lsu.persistence.model.shift;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.csudh.lsu.persistence.model.View;
import edu.csudh.lsu.persistence.model.common.Common;
import groovy.transform.ToString;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.UUID;

@ToString
@Entity
@Table(name = "SHIFT_TOTAL")
@JsonDeserialize
@JsonSerialize
@NoArgsConstructor
public class ShiftTotal extends Common {

    @Getter
    @Setter
    @JsonView(View.Json.class)
    @Column(name = "ID", nullable = false)
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
    @Column(name = "GAME", nullable = false)
    private String game;

    @Getter
    @Setter
    @JsonView(View.Json.class)
    @Column(name = "COST", nullable = false)
    private String cost;

    @Getter
    @Setter
    @JsonView(View.Json.class)
    @Column(name = "PAYMENT_MODE", nullable = false)
    private String paymentMode;

    @Getter
    @Setter
    @JsonView(View.Json.class)
    @Column(name = "DURATION", nullable = false)
    private String duration;

    @Getter
    @Setter
    @JsonView(View.Json.class)
    @Column(name = "ATTENDANT_CLOCKED_IN_DATE", nullable = false)
    private Date clockedInDate;

}
