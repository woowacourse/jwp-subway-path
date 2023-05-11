package subway.exception;

public enum ErrorCode {
    DUPLICATE_NAME("중복된 이름입니다."),
    DUPLICATE_STATION("이미 존재하는 역입니다."),
    NOT_FOUND_STATION("존재하지 않는 역입니다."),
    INVALID_DISTANCE("기존에 존재하는 역 사이의 거리보다 작아야 합니다."),
    INVALID_BLANK_NAME("이름은 공백이 될 수 없습니다."),
    INVALID_NOT_POSITIVE_DISTANCE("거리는 양수여야 합니다."),
    INVALID_SAME_UP_AND_DOWN_STATION("시작역과 도착역은 같을 수 없습니다.");

    private final String errorMessage;

    ErrorCode(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
