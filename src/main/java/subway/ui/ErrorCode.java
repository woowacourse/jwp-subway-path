package subway.ui;

public enum ErrorCode {

    STATION_NOT_EXIST("존재하지 않는 역입니다."),
    INVALID_SECTION_LENGTH("등록될 수 없는 길이의 구간입니다. 1이상의 정수가 아니거나, 너무 큰 길이입니다."),
    NOT_FOUND_END_STATION("종점을 찾을 수 없습니다."),
    NOT_FOUND_SECTION("해당 구간을 찾을 수 없습니다."),
    INVALID_SECTION_CONNECTION("이어질 수 없는 구간들 입니다. 노선은 일직선으로, 순환노선이 없어야 합니다.");


    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
