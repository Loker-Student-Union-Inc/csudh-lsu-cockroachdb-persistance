package edu.csudh.lsu.persistence.model.profile;

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

@ToString
@Entity
@Table(name = "PROFILE")
@JsonDeserialize
@JsonSerialize
@NoArgsConstructor
public class Profile extends Common {

    @Getter
    @Setter
    @Id
    @JsonView(View.Json.class)
    @Column(name = "USER_ID", nullable = false)
    private String userId;

    @Getter
    @Setter
    @JsonView(View.Json.class)
    @Column(name = "USER_PASSWORD", nullable = false)
    private String userPassword;

    @Getter
    @Setter
    @JsonView(View.Json.class)
    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Getter
    @Setter
    @JsonView(View.Json.class)
    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    @Getter
    @Setter
    @JsonView(View.Json.class)
    @Column(name = "ROLE", nullable = false)
    private String role;

    @Getter
    @Setter
    @JsonView(View.Json.class)
    @Column(name = "PERMISSION", nullable = false)
    private String permission;  // Json Object
}
