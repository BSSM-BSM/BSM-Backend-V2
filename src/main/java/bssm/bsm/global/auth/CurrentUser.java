package bssm.bsm.global.auth;

import bssm.bsm.domain.user.domain.User;
import bssm.bsm.domain.user.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrentUser {

    private final UserFacade userFacade;

    public User getUser() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return userFacade.findCachedUserByCode(Long.parseLong(userId));
    }

    public User getUserOrNull() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userId.equals("anonymousUser")) return null;
        return userFacade.findCachedUserByCode(Long.parseLong(userId));
    }

}
