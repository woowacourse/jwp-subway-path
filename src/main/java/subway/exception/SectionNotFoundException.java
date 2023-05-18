package subway.exception;

import subway.controller.exception.NotFoundException;

public class SectionNotFoundException extends NotFoundException {

    private static final String MESSAGE = "존재하지 않는 구간입니다.";

    public SectionNotFoundException() {
        super(MESSAGE);
    }
}
