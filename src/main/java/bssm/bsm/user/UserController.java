package bssm.bsm.user;

import bssm.bsm.global.utils.UserUtil;
import bssm.bsm.user.dto.request.UserSignUpDto;
import bssm.bsm.user.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserUtil userUtil;

    @GetMapping()
    public User getUserInfo() {
        return userUtil.getCurrentUser();
    }

    @PostMapping()
    public void signUp(@RequestBody UserSignUpDto dto) throws Exception {
        userService.signUp(dto);
    }

}
