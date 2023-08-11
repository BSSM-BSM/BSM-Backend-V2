package bssm.bsm.domain.school.timetable.exception;

import bssm.bsm.global.error.exceptions.NotFoundException;

public class NoSuchTimetableException extends NotFoundException {
    public NoSuchTimetableException() {
        super("시간표를 찾을 수 없습니다.");
    }
}