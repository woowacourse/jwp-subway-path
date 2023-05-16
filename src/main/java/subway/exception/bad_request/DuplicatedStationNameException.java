package subway.exception.bad_request;

import subway.exception.bad_request.BadRequestException;

public class DuplicatedStationNameException extends BadRequestException {

    private static final String MESSAGE = "이미 존재하는 역입니다.";

    public DuplicatedStationNameException() {
        super(MESSAGE);
    }
}
