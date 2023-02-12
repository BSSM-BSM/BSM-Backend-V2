package bssm.bsm.domain.webpush.service;

import bssm.bsm.domain.user.domain.User;
import bssm.bsm.domain.webpush.domain.WebPush;
import bssm.bsm.domain.webpush.domain.repository.WebPushRepository;
import bssm.bsm.domain.webpush.presentation.dto.request.WebPushSubscribeRequest;
import bssm.bsm.global.error.exceptions.NotFoundException;
import bssm.bsm.global.auth.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebPushService {

    private final CurrentUser currentUser;
    private final WebPushRepository webpushRepository;

    public void subscribe(WebPushSubscribeRequest dto) {
        User user = currentUser.getUser();

        WebPush webPush = webpushRepository.findByUserCodeAndEndpoint(user.getCode(), dto.getEndpoint())
                .orElseGet(() -> WebPush.builder()
                        .endpoint(dto.getEndpoint())
                        .auth(dto.getAuth())
                        .p256dh(dto.getP256dh())
                        .userCode(user.getCode())
                        .build()
                );

        webPush.updateWebPush(dto);
        webpushRepository.save(webPush);
    }

    public void unsubscribe(String endpoint) {
        WebPush webPush = webpushRepository.findById(endpoint).orElseThrow(NotFoundException::new);
        webpushRepository.delete(webPush);
    }

}
