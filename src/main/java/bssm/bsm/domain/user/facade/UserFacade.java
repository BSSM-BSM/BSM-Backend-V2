package bssm.bsm.domain.user.facade;

import bssm.bsm.domain.user.presentation.dto.response.UserResponseDto;
import bssm.bsm.domain.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserFacade {

    public UserResponseDto toBoardUserResponse(User user, boolean anonymous) {
        if (anonymous) {
            return UserResponseDto.builder()
                    .code((long) -1)
                    .nickname("ㅇㅇ")
                    .build();
        }
        return UserResponseDto.builder()
                .code(user.getCode())
                .nickname(user.getNickname())
                .build();
    }

}
