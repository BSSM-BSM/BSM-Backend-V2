package bssm.bsm.domain.user.presentation;

import bssm.bsm.domain.user.presentation.dto.res.UserDetailRes;
import bssm.bsm.global.auth.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final CurrentUser currentUser;

    @GetMapping
    public UserDetailRes getUserInfo() {
        return currentUser.getUser().toUserInfoResponse();
    }

}
