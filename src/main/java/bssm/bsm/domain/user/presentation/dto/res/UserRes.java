package bssm.bsm.domain.user.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserRes {

    private Long code;
    private String nickname;
}
