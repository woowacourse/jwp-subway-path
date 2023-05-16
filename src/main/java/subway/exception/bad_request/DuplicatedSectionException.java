package subway.exception.bad_request;

import subway.exception.bad_request.BadRequestException;

public class DuplicatedSectionException extends BadRequestException {

    private static final String MESSAGE = "같은 노선에 중복된 구간이 존재합니다.";

    public DuplicatedSectionException() {
        super(MESSAGE);
    }
}
