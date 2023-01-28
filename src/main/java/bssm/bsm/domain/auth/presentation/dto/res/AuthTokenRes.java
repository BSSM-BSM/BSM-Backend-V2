package bssm.bsm.domain.auth.presentation.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthTokenRes {

    private String accessToken;
    private String refreshToken;
}
