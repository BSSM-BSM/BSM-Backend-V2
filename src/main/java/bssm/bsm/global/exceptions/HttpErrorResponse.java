package bssm.bsm.global.exceptions;

import lombok.Getter;

@Getter
public class HttpErrorResponse {

    private final int statusCode;
    private final String message;

    public HttpErrorResponse(HttpError httpError) {
        this.statusCode = httpError.getStatusCode();
        this.message = httpError.getMessage();
    }
}
