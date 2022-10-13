package bssm.bsm.domain.webpush.presentation.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WebPushSendDto {

    private String title;
    private String body;
    private String link;

}
