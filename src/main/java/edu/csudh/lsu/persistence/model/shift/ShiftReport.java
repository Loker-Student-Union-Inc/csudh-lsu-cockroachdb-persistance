package edu.csudh.lsu.persistence.model.shift;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.csudh.lsu.persistence.model.View;
import edu.csudh.lsu.persistence.model.common.Common;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@ToString
@Entity
@Table(name = "SHIFT_REPORT")
@JsonDeserialize
@JsonSerialize
@NoArgsConstructor
public class ShiftReport extends Common {

    @Getter
    @Setter
    @JsonView(View.Json.class)
    @Column(name = "SHIFT_REPORT_TD", nullable = false)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Id
    private UUID shiftReportId;

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
    @Column(name = "ATTENDANT_SIGN", nullable = false)
    private String attendantSign;

    @Getter
    @Setter
    @JsonView(View.Json.class)
    @Column(name = "REVENUE_IN_CARD", nullable = false)
    private Float revenueInCard;

    @Getter
    @Setter
    @JsonView(View.Json.class)
    @Column(name = "REVENUE_IN_CASH", nullable = false)
    private Float revenueInCash;

    @Getter
    @Setter
    @JsonView(View.Json.class)
    @Column(name = "SHIFT_TOTAL", nullable = false)
    private String shiftTotal;

    @Getter
    @Setter
    @JsonView(View.Json.class)
    @Column(name = "OPENING_BALANCE", nullable = false)
    private Float openingBalance;

    @PrePersist
    private void onCreate() {
        this.closingShiftTime = Time.valueOf(LocalTime.now());
        this.closingShiftDate = Date.valueOf(LocalDate.now());
    }
}
