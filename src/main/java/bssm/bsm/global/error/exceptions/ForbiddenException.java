package bssm.bsm.global.error.exceptions;

import bssm.bsm.global.error.HttpException;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ForbiddenException extends HttpException {

    private final int statusCode = 403;
    private String message = "Forbidden";

    public ForbiddenException(String message) {
        this.message = message;
    }
}