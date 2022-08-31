package bssm.bsm.domain.school.meal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class RawMealItemDto {

    private String DDISH_NM; // 급식 식단 정보 문자열
    private String MLSV_YMD; // 급식 날짜
    private String MMEAL_SC_NM; // 급식 시간
}
