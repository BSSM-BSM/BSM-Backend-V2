package bssm.bsm.global.error.exceptions;

import bssm.bsm.global.error.HttpError;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ConflictException extends HttpError {

    private final int statusCode = 409;
    private String message = "Conflict";

    public ConflictException(String message) {
        this.message = message;
    }
}