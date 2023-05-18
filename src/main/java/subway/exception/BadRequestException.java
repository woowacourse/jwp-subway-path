package subway.exception;

public final class BadRequestException extends GlobalException {

    private static final String messageFormat = "잘못된 값을 입력하셨습니다. 입력값 : %s, 정상적인 입력 예시 : %s";

    public BadRequestException(final String wrongMessage, final String correctMessage) {
        super(String.format(messageFormat, wrongMessage, correctMessage));
    }

    public BadRequestException(final Integer wrongPrice, final String errorMessage) {
        super(String.format(messageFormat, wrongPrice, errorMessage));
    }
}
