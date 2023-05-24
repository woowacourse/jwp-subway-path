package subway.ui;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    STATION_NOT_EXIST(HttpStatus.BAD_REQUEST.value(), "존재하지 않는 역입니다."),
    STATION_ALREADY_EXIST(HttpStatus.BAD_REQUEST.value(), "이미 존재하는 역입니다."),
    INVALID_SECTION_LENGTH(HttpStatus.BAD_REQUEST.value(), "등록될 수 없는 길이의 구간입니다. 1이상의 정수가 아니거나, 너무 큰 길이입니다."),
    NOT_FOUND_END_STATION(HttpStatus.BAD_REQUEST.value(),"종점을 찾을 수 없습니다."),
    NOT_FOUND_SECTION(HttpStatus.BAD_REQUEST.value(),"해당 구간을 찾을 수 없습니다."),
    INVALID_SECTION_CONNECTION(HttpStatus.BAD_REQUEST.value(),"이어질 수 없는 구간들 입니다. 노선은 일직선으로, 순환노선이 없어야 합니다.");


    private final int status;
    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
