package bssm.bsm.global.auth;

import bssm.bsm.global.error.exceptions.NotFoundException;
import bssm.bsm.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userCode) throws UsernameNotFoundException {
        return userRepository.findById(Long.valueOf(userCode))
                .map(AuthDetails::new)
                .orElseThrow(() -> {throw new NotFoundException();});
    }
}
