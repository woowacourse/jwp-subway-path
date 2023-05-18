package subway.exception;

import subway.controller.exception.BadRequestException;

public class InvalidDirectionException extends BadRequestException {

    private static final String MESSAGE = "잘못된 방향입니다.";

    public InvalidDirectionException() {
        super(MESSAGE);
    }
}
