package bssm.bsm.global.auth.exception;

import bssm.bsm.global.error.exceptions.BadRequestException;
import com.google.common.collect.ImmutableMap;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class InvalidApiTokenException extends BadRequestException {

    private static Map<String, String> createMsg() {
        return ImmutableMap.<String, String>builder()
                .put("errorType", "apiTokenFail")
                .build();
    }

    private static Map<String, String> createMsg(ZonedDateTime serverTime, ZonedDateTime clientTime) {
        return ImmutableMap.<String, String>builder()
                .put("errorType", "apiTokenFail")
                .put("serverTime", serverTime.format(DateTimeFormatter.ISO_INSTANT))
                .put("clientTime", clientTime.format(DateTimeFormatter.ISO_INSTANT))
                .build();
    }


    public InvalidApiTokenException() {
        super(createMsg());
    }

    public InvalidApiTokenException(ZonedDateTime serverTime, ZonedDateTime clientTime) {
        super(createMsg(serverTime, clientTime));
    }
}