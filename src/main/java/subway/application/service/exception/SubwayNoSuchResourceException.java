package subway.application.service.exception;

import subway.common.exception.SubwayException;

public class SubwayNoSuchResourceException extends SubwayException {

    public SubwayNoSuchResourceException(final String message) {
        super(message);
    }
}
