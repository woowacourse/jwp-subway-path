package subway.line.application.exception;

import org.springframework.http.HttpStatus;
import subway.common.exception.BadRequestException;

public class SectionSavingNotPossibleException extends BadRequestException {
    private static final String MESSAGE = "주어진 정보로 구간을 추가할 수 없습니다.";

    public SectionSavingNotPossibleException() {
        super(MESSAGE);
    }

    public SectionSavingNotPossibleException(Throwable cause) {
        super(MESSAGE, cause);
    }

    protected SectionSavingNotPossibleException(Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(MESSAGE, cause, enableSuppression, writableStackTrace);
    }
}
