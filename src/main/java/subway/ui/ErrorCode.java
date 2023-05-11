package subway.ui;

public enum ErrorCode {
    STATION_NOT_EXIST("존재하지 않는 역입니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
