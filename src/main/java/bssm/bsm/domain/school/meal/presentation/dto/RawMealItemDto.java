package bssm.bsm.domain.school.meal.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RawMealItemDto {

    private String DDISH_NM; // 급식 식단 정보 문자열
    private String MLSV_YMD; // 급식 날짜
    private String MMEAL_SC_NM; // 급식 시간
}
