package subway.exception;

import subway.controller.exception.NotFoundException;

public class LineOrStationNotFoundException extends NotFoundException {

    private static final String MESSAGE = "노선이나 역을 찾을 수 없습니다.";

    public LineOrStationNotFoundException() {
        super(MESSAGE);
    }
}
