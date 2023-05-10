package subway.exception.line;

import org.springframework.http.HttpStatus;
import subway.exception.BaseExceptionType;

public enum LineExceptionType implements BaseExceptionType {

    NOT_FOUND_LINE(
            200,
            HttpStatus.NOT_FOUND,
            "노선이 존재하지 않습니다."
    ),
    DUPLICATE_LINE_NAME(
            201,
            HttpStatus.CONFLICT,
            "이미 등록된 노선 이름입니다."
    ),
    ;

    private final int errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    LineExceptionType(final int errorCode, final HttpStatus httpStatus, final String errorMessage) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    @Override
    public int errorCode() {
        return errorCode;
    }

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }

    @Override
    public String errorMessage() {
        return errorMessage;
    }
}
