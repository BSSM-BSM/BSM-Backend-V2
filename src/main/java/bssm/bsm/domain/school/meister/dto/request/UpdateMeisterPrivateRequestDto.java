package bssm.bsm.domain.school.meister.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class UpdateMeisterPrivateRequestDto {

    private boolean privateRanking;
}
