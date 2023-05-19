package subway.payment.exception;

import subway.common.exception.BaseException;
import subway.common.exception.BaseExceptionType;

public class PaymentException extends BaseException {

    private final PaymentExceptionType exceptionType;

    public PaymentException(final PaymentExceptionType exceptionType) {
        super(exceptionType.errorMessage());
        this.exceptionType = exceptionType;
    }

    public PaymentException(final String message) {
        super(String.format(PaymentExceptionType.ABNORMAL_EXCEPTION.errorMessage(), message));
        this.exceptionType = PaymentExceptionType.ABNORMAL_EXCEPTION;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}

