package bssm.bsm.global.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ForbiddenException extends HttpError {

    private final int statusCode = 403;
    private String message = "Forbidden";

    public ForbiddenException(String message) {
        this.message = message;
    }
}