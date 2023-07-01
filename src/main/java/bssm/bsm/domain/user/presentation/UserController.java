package bssm.bsm.domain.user.presentation;

import bssm.bsm.domain.user.presentation.dto.res.UserDetailRes;
import bssm.bsm.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public UserDetailRes getUserInfo() {
        return userService.findMyInfo();
    }

}
