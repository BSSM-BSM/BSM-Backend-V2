package bssm.bsm.school.meister.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeisterResponseDto {

    private Integer score;
    private Integer positivePoint;
    private Integer negativePoint;
    private String uniqNo;
    private Boolean loginError;
}
