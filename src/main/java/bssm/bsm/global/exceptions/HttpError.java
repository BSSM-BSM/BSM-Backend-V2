package bssm.bsm.global.exceptions;

import lombok.Getter;

@Getter
public class HttpError extends RuntimeException {

    private final int statusCode = 500;
    private final String message = "Internal Server Error";
}
