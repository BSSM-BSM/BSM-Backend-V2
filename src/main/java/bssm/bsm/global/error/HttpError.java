package bssm.bsm.global.error;

import lombok.Getter;

@Getter
public class HttpError extends RuntimeException {

    private final int statusCode = 500;
    private final String message = "Internal Server Error";
}
