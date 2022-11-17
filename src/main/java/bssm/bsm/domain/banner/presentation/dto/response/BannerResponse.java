package bssm.bsm.domain.banner.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BannerResponse {

    private String id;
    private String ext;
    private String link;

}
