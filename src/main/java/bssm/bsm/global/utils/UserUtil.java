package bssm.bsm.global.utils;

import bssm.bsm.domain.user.domain.UserRepository;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.global.error.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserUtil {

    private final UserRepository userRepository;

    public User getUser() {
        String userCode = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findById(Long.valueOf(userCode))
                .orElseThrow(NotFoundException::new);
    }

    public Optional<User> getOptionalUser() {
        String userCode = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userCode.equals("anonymousUser")) return Optional.empty();
        return userRepository.findById(Long.valueOf(userCode));
    }

}
