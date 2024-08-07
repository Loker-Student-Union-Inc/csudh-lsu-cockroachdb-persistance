package edu.csudh.lsu.persistence.model.common;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;

@JsonSerialize
@JsonDeserialize
@NoArgsConstructor
public class Common {

    @Getter
    @Setter
    private Time createTime;

    @Getter
    @Setter
    private Date createDate;

    @Getter
    @Setter
    private String creator; // Creator Name (Created by)

    @Getter
    @Setter
    private String user;    // User Name
}
