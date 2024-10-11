package bssm.bsm.domain.webpush.presentation.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class WebPushSubscribeRequest {

    @NotBlank
    private String endpoint;

    @NotBlank
    @Size(min = 22, max = 22)
    private String auth;

    @NotBlank
    @Size(min = 87, max = 87)
    private String p256dh;

}
