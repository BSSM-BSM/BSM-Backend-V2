package bssm.bsm.domain.webpush.presentation;

import bssm.bsm.domain.webpush.presentation.dto.request.WebPushSubscribeRequest;
import bssm.bsm.domain.webpush.service.WebPushService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("webpush")
@RequiredArgsConstructor
public class WebPushController {

    private final WebPushService webPushService;

    @PostMapping
    public void subscribe(@RequestBody @Valid WebPushSubscribeRequest dto) {
        webPushService.subscribe(dto);
    }

    @DeleteMapping
    public void unsubscribe() {
        webPushService.unsubscribe();
    }

}
