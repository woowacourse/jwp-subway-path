package subway.payment.exception;

import org.springframework.http.HttpStatus;
import subway.common.exception.BaseExceptionType;

public enum PaymentExceptionType implements BaseExceptionType {

    ABNORMAL_EXCEPTION(
            499,
            HttpStatus.INTERNAL_SERVER_ERROR,
            "발생하면 안되는 예외 발생 (설명 : %s)"
    ),
    ;

    private final int errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    PaymentExceptionType(final int errorCode, final HttpStatus httpStatus, final String errorMessage) {
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
