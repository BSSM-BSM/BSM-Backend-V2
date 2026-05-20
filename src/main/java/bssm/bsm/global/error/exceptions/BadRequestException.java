package bssm.bsm.global.error.exceptions;

import bssm.bsm.global.error.HttpException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class BadRequestException extends HttpException {

    private final int statusCode = 400;
    private final Map<String, String> fields;
}