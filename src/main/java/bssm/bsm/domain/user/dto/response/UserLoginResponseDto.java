package bssm.bsm.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserLoginResponseDto {

    private String accessToken;
    private String refreshToken;
}
