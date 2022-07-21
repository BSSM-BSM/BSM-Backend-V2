package bssm.bsm.user;

import bssm.bsm.user.dto.request.UserSignUpDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public void signUp(@RequestBody UserSignUpDto dto) throws Exception {
        userService.signUp(dto);
    }

}
