package bssm.bsm.global.error.exceptions;

import bssm.bsm.global.error.HttpException;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotFoundException extends HttpException {

    private final int statusCode = 404;
    private String message = "Not Found";

    public NotFoundException(String message) {
        this.message = message;
    }
}