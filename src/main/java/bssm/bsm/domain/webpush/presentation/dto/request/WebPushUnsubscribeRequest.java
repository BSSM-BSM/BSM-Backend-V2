package bssm.bsm.domain.webpush.presentation.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class WebPushUnsubscribeRequest {

    @NotBlank
    private String endpoint;

}
