package subway.exception.bad_request;

import subway.exception.bad_request.BadRequestException;

public class InvalidDistanceException extends BadRequestException {

    private static final String MESSAGE = "구간 간격이 너무 큽니다.";

    public InvalidDistanceException() {
        super(MESSAGE);
    }
}
