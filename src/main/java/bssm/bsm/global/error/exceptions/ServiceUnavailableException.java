package bssm.bsm.global.error.exceptions;

import bssm.bsm.global.error.HttpException;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ServiceUnavailableException extends HttpException {

    private final int statusCode = 503;
    private String message = "Service Unavailable";

    public ServiceUnavailableException(String message) {
        this.message = message;
    }
}