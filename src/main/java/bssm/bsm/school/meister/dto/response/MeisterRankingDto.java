package bssm.bsm.school.meister.dto.response;

import bssm.bsm.user.entities.Student;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MeisterRankingDto {

    private Integer score;
    private Integer positivePoint;
    private Integer negativePoint;
    private LocalDateTime lastUpdate;
    private Student student;
    private Boolean loginError;
}
