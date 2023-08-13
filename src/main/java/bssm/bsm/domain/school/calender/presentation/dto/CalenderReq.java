package bssm.bsm.domain.school.calender.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CalenderReq {
    @JsonProperty
    private int month;
    @JsonProperty
    private int day;
    @JsonProperty
    private int classNo;
    @JsonProperty
    private int grade;
    @JsonProperty
    private String title;
    @JsonProperty
    private String content;
    @JsonProperty
    private String color;
}
