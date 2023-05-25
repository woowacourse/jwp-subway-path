package subway.line.application.exception;

import subway.common.exception.BadRequestException;

public class SectionDeletingNotPossibleException extends BadRequestException {
    private static final String MESSAGE = "주어진 정보로 구간을 삭제할 수 없습니다.";

    public SectionDeletingNotPossibleException() {
        super(MESSAGE);
    }

    public SectionDeletingNotPossibleException(Throwable cause) {
        super(MESSAGE, cause);
    }

    protected SectionDeletingNotPossibleException(Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(MESSAGE, cause, enableSuppression, writableStackTrace);
    }
}
