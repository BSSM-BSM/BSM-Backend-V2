package bssm.bsm.global;

import bssm.bsm.global.exceptions.BadRequestException;
import bssm.bsm.global.exceptions.HttpError;
import bssm.bsm.global.exceptions.HttpErrorResponse;
import bssm.bsm.global.exceptions.InternalServerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpError.class)
    public ResponseEntity<HttpErrorResponse> handleException(HttpError exception) {
        HttpErrorResponse httpErrorResponse = new HttpErrorResponse(exception);
        return new ResponseEntity<>(httpErrorResponse, HttpStatus.valueOf(exception.getStatusCode()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<HttpErrorResponse> handleException(ConstraintViolationException exception) {
        HttpErrorResponse httpErrorResponse = new HttpErrorResponse(new BadRequestException(exception.getMessage()));
        return new ResponseEntity<>(httpErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpErrorResponse> handleException(Exception e) {
        e.printStackTrace();
        HttpErrorResponse httpErrorResponse = new HttpErrorResponse(new HttpError());
        return new ResponseEntity<>(httpErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}