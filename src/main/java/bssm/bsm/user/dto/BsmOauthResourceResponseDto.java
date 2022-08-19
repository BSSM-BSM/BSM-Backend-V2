package bssm.bsm.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class BsmOauthResourceResponseDto {

    private UserSignUpDto user;

    @JsonProperty("user")
    private void unpackNested(Map<String, Object> user) {
        this.user = UserSignUpDto.builder()
                .usercode((int) user.get("code"))
                .nickname((String) user.get("nickname"))
                .enrolledAt((int) user.get("enrolledAt"))
                .grade((int) user.get("grade"))
                .classNo((int) user.get("classNo"))
                .studentNo((int) user.get("studentNo"))
                .build();
    }
}
