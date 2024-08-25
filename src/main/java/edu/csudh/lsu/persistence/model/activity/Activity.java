package edu.csudh.lsu.persistence.model.activity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.csudh.lsu.persistence.model.common.Common;
import lombok.ToString;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@ToString
@Entity
@Table(name = "ACTIVITY")
@JsonDeserialize
@JsonSerialize
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Activity extends Common {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "ID", updatable = true, nullable = false)
    @Getter @Setter
    private UUID id;

    @Column(name = "ACTIVITY", updatable = true, nullable = false)
    @Getter @Setter
    private String activity;

    @Column(name = "CATEGORY", updatable = true, nullable = false)  // Pool table, Console(Ps5, ps4, xbox, switch) and, board games
    @Getter @Setter
    private String category;

    @Column(name = "PRICE", updatable = true)
    @Getter @Setter
    private String price;   // price for 30 minutes

    @Column(name = "IMAGE_LOCATION", updatable = true)
    @Getter @Setter
    private String imageLocation;   // image location pointing to dropping
}