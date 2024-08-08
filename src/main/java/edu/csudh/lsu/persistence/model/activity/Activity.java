package edu.csudh.lsu.persistence.model.activity;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.csudh.lsu.persistence.model.View;
import edu.csudh.lsu.persistence.model.common.Common;
import groovy.transform.ToString;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@ToString
@Entity
@Table(name = "ACTIVITY")
@JsonDeserialize
@JsonSerialize
@NoArgsConstructor
public class Activity extends Common {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "ID", updatable = true, nullable = false)
    @Getter @Setter
    @JsonView(View.Json.class)
    private UUID id;

    @Column(name = "ACTIVITY", updatable = true, nullable = false)
    @Getter @Setter
    @JsonView(View.Json.class)
    private String activity;

    @Column(name = "CATEGORY", updatable = true, nullable = false)  // Pool table, Console(Ps5, ps4, xbox, switch) and, board games
    @Getter @Setter
    @JsonView(View.Json.class)
    private String category;

    @Column(name = "PRICE", updatable = true)
    @Getter @Setter
    @JsonView(View.Json.class)
    private Integer price;   // price for 30 minutes

    @Column(name = "IMAGE_LOCATION", updatable = true)
    @Getter @Setter
    @JsonView(View.Json.class)
    private String imageLocation;   // image location pointing to dropping
}
