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
import java.sql.Time;

@ToString
@Entity
@Table(name = "SHIFT_REPORT")
@JsonDeserialize
@JsonSerialize
@NoArgsConstructor
public class ShiftReport extends Common {

    @Getter
    @Setter
    @Id
    @Column(name = "SHIFT_ID", nullable = false)
    private String shiftId; // user id + date + shift #

    @Getter
    @Setter
    @JsonView(View.Json.class)
    @Column(name = "CLOSING_SHIFT_DATE", nullable = false)
    private Date closingShiftDate;

    @Getter
    @Setter
    @JsonView(View.Json.class)
    @Column(name = "CLOSING_SHIFT_Time", nullable = false)
    private Time closingShiftTime;

    @Getter
    @Setter
    @JsonView(View.Json.class)
    @Column(name = "ATTENDANT_NAME", nullable = false)
    private String attendantName;

    @Getter
    @Setter
    @JsonView(View.Json.class)
    @Column(name = "RECONCILOR_NAME", nullable = false)
    private String reconcilorName;

    @Getter
    @Setter
    @JsonView(View.Json.class)
    @Column(name = "RECONCILOR_SIGN", nullable = false)
    private String reconcilorSign;

    @Getter
    @Setter
    @JsonView(View.Json.class)
    @Column(name = "RECONCILOR_SIGN", nullable = false)
    private String attendantSign;

    @Getter
    @Setter
    @JsonView(View.Json.class)
    @Column(name = "REVENUE_IN_CARD", nullable = false)
    private String revenueInCard;

    @Getter
    @Setter
    @JsonView(View.Json.class)
    @Column(name = "REVENUE_IN_CASH", nullable = false)
    private String revenueInCash;

    @Getter
    @Setter
    @JsonView(View.Json.class)
    @Column(name = "SHIFT_TOTAL", nullable = false)
    private String shiftTotal;

    @Getter
    @Setter
    @JsonView(View.Json.class)
    @Column(name = "OPENING_BALANCE", nullable = false)
    private String openingBalance;
}
