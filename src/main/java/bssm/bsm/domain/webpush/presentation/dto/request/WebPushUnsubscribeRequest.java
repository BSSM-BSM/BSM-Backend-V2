package bssm.bsm.domain.webpush.presentation.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class WebPushUnsubscribeRequest {

    @NotBlank
    private String endpoint;

}
