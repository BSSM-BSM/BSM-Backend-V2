package bssm.bsm.global.auth;

import bssm.bsm.domain.user.facade.UserFacade;
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

    private final UserFacade userFacade;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return new AuthDetails(
                userFacade.findCachedUserByCode(Long.parseLong(userId))
        );
    }
}
