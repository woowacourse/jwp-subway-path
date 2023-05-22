package subway.exception;

public enum ErrorMessage {
    DUPLICATE_NAME("중복된 이름입니다."),
    DUPLICATE_STATION("이미 존재하는 역입니다."),
    DUPLICATE_SECTION("이미 존재하는 구간입니다."),
    DUPLICATE_LINE("이미 존재하는 노선입니다."),
    NOT_FOUND_STATION("존재하지 않는 역입니다."),
    NOT_FOUND_SECTION("존재하지 않는 구간입니다."),
    NOT_FOUND_SECTION_BY_LINE_ID("해당 노선의 구간을 찾을 수 없습니다."),
    NOT_FOUND_LINE("존재하지 않는 노선입니다."),
    INVALID_DISTANCE("기존에 존재하는 역 사이의 거리보다 작아야 합니다."),
    INVALID_NOT_POSITIVE_ID("ID는 양수여야 합니다"),
    INVALID_BLANK_NAME("이름은 공백이 될 수 없습니다."),
    INVALID_NOT_POSITIVE_DISTANCE("거리는 양수여야 합니다."),
    INVALID_SAME_UP_AND_DOWN_STATION("시작역과 도착역은 같을 수 없습니다."),
    INVALID_DELETE_SECTION_REQUEST("노선에 구간이 존재하지 않아 삭제할 수 없습니다."),
    INVALID_NEGATIVE_FARE("요금은 음수가 될 수 없습니다."),
    ;

    private final String errorMessage;

    ErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
