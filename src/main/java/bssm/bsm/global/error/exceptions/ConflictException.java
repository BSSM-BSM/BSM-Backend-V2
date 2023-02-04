package bssm.bsm.global.error.exceptions;

import bssm.bsm.global.error.HttpException;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ConflictException extends HttpException {

    private final int statusCode = 409;
    private String message = "Conflict";

    public ConflictException(String message) {
        this.message = message;
    }
}