package bssm.bsm.domain.user.service;

import bssm.bsm.domain.user.domain.User;
import bssm.bsm.domain.user.presentation.dto.res.UserDetailRes;
import bssm.bsm.global.auth.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final CurrentUser currentUser;

    public UserDetailRes findMyInfo() {
        User user = currentUser.getUser();
        return UserDetailRes.create(user);
    }

}
