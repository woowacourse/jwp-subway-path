package subway.exception;

public enum ErrorCode {
    DUPLICATE_NAME("중복된 이름입니다.");

    private final String errorMessage;

    ErrorCode(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
