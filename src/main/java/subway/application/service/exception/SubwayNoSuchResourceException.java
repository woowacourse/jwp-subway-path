package subway.application.service.exception;

import subway.exception.SubwayException;

public class SubwayNoSuchResourceException extends SubwayException {

    public SubwayNoSuchResourceException(final String message) {
        super(message);
    }
}
