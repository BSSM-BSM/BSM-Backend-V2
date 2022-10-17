package bssm.bsm.domain.user.facade;

import bssm.bsm.domain.user.domain.Student;
import bssm.bsm.domain.user.presentation.dto.response.StudentInfoResponse;
import bssm.bsm.domain.user.presentation.dto.response.UserInfoResponse;
import bssm.bsm.domain.user.presentation.dto.response.UserResponse;
import bssm.bsm.domain.user.domain.User;
import leehj050211.bsmOauth.dto.response.BsmResourceResponse;
import leehj050211.bsmOauth.dto.response.BsmStudentResponse;
import leehj050211.bsmOauth.dto.response.BsmTeacherResponse;
import org.springframework.stereotype.Component;

@Component
public class UserFacade {

    public UserResponse toBoardUserResponse(User user, boolean anonymous) {
        if (anonymous) {
            return UserResponse.builder()
                    .code((long) -1)
                    .nickname("ㅇㅇ")
                    .build();
        }
        return UserResponse.builder()
                .code(user.getCode())
                .nickname(user.getNickname())
                .build();
    }

}
