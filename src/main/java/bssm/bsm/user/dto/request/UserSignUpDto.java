package bssm.bsm.user.dto.request;

import bssm.bsm.user.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSignUpDto {
    private String id;

    private String pw;

    private String checkPw;

    private String nickname;

    private String authCode;

    public User toEntity() {
        return User.builder()
                .id(id)
                .nickname(nickname)
                .build();
    }
}
