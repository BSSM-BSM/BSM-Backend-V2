package bssm.bsm.domain.user.dto.response;

import bssm.bsm.domain.user.type.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {

    private Long code;
    private String nickname;
}
