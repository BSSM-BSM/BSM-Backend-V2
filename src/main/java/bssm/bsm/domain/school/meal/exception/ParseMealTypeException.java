package bssm.bsm.domain.school.meal.exception;

import bssm.bsm.global.error.exceptions.InternalServerException;

public class ParseMealTypeException extends InternalServerException {
    public ParseMealTypeException() {
        super("급식 배식 시간 파싱에 문제가 발생하였습니다.");
    }
}
