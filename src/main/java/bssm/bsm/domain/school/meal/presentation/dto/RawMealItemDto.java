package bssm.bsm.domain.school.meal.presentation.dto;

import bssm.bsm.domain.school.meal.domain.Meal;
import bssm.bsm.domain.school.meal.domain.MealPk;
import bssm.bsm.domain.school.meal.domain.MealType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@AllArgsConstructor
public class RawMealItemDto {

    private String DDISH_NM; // 급식 식단 정보 문자열
    private String MLSV_YMD; // 급식 날짜
    private String MMEAL_SC_NM; // 급식 시간
    private String CAL_INFO; // 칼로리 정보

    public Meal toEntity(String content) {
        float cal = 0;
        Matcher calMatch = Pattern.compile("[0-9.]+").matcher(CAL_INFO);
        if (calMatch.find()) cal = Float.parseFloat(calMatch.group());

        MealPk.MealPkBuilder Pkbuilder = MealPk.builder()
                .date(LocalDate.parse(MLSV_YMD, DateTimeFormatter.ofPattern("yyyyMMdd")));
        switch (MMEAL_SC_NM) {
            case "조식" -> Pkbuilder = Pkbuilder.type(MealType.MORNING);
            case "중식" -> Pkbuilder = Pkbuilder.type(MealType.LUNCH);
            case "석식" -> Pkbuilder = Pkbuilder.type(MealType.DINNER);
        }
        return Meal.builder()
                .pk(Pkbuilder.build())
                .content(content)
                .cal(cal)
                .build();
    }

}
