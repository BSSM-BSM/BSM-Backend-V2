package bssm.bsm.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class UserUpdatePwDto {

    private String newPw;
    private String checkNewPw;
}
