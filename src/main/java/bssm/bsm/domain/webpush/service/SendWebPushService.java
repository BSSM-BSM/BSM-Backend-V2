package bssm.bsm.domain.webpush.service;

import bssm.bsm.domain.webpush.domain.WebPush;
import bssm.bsm.domain.webpush.presentation.dto.request.WebPushMsgDto;
import bssm.bsm.global.async.AsyncService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Subscription;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SendWebPushService {

    private final ObjectMapper objectMapper;
    private final PushService pushService;
    private final AsyncService asyncService;

    @Async("threadPoolTaskExecutor")
    public void sendNotification(WebPush webPush, WebPushMsgDto dto) throws Exception {
        Subscription subscription = toSubscription(webPush);
        String msg = objectMapper.writeValueAsString(dto);
        send(subscription, msg);
    }

    @Async("threadPoolTaskExecutor")
    public void sendNotificationToAll(List<WebPush> webPushList, WebPushMsgDto dto) throws JsonProcessingException {
        List<Subscription> subscriptionList = webPushList.stream()
                .map(this::toSubscription)
                .toList();
        String msg = objectMapper.writeValueAsString(dto);

        subscriptionList.forEach(subscription -> send(subscription, msg));
    }

    public void send(Subscription subscription, String msg) {
        asyncService.run(() -> {
            try {
                pushService.send(new Notification(subscription, msg));
            } catch (Exception ignored) {}
        });
    }

    private Subscription toSubscription(WebPush webPush) {
        return new Subscription(webPush.getEndpoint(), new Subscription.Keys(webPush.getP256dh(), webPush.getAuth()));
    }

}
