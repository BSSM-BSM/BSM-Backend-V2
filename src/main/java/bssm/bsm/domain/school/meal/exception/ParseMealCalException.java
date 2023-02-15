package bssm.bsm.domain.school.meal.exception;

import bssm.bsm.global.error.exceptions.InternalServerException;

public class ParseMealCalException extends InternalServerException {
    public ParseMealCalException() {
        super("급식 칼로리 정보 파싱에 문제가 발생하였습니다.");
    }
}
