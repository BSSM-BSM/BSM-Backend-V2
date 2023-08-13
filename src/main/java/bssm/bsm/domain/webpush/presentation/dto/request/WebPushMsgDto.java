package bssm.bsm.domain.webpush.presentation.dto.request;

import lombok.Getter;

@Getter
public class WebPushMsgDto {

    private String title;
    private String body;
    private String link;

    public static WebPushMsgDto create(String title, String body, String link) {
        WebPushMsgDto dto = new WebPushMsgDto();
        dto.title = title;
        dto.body = body;
        dto.link = link;
        return dto;
    }
}
