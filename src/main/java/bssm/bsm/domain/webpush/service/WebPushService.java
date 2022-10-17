package bssm.bsm.domain.webpush.service;

import bssm.bsm.domain.user.domain.User;
import bssm.bsm.domain.webpush.domain.WebPush;
import bssm.bsm.domain.webpush.domain.repository.WebPushRepository;
import bssm.bsm.domain.webpush.presentation.dto.request.WebPushSubscribeRequest;
import bssm.bsm.global.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WebPushService {

    private final UserUtil userUtil;
    private final WebPushRepository webpushRepository;

    public void subscribe(WebPushSubscribeRequest dto) {
        User user = userUtil.getUser();

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

    public void unsubscribe() {
        List<WebPush> webPushList = webpushRepository.findAllByUserCode(userUtil.getUser().getCode());
        webpushRepository.deleteAll(webPushList);
    }

}
