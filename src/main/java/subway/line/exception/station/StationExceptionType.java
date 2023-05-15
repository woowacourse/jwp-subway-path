package subway.line.exception.station;

import org.springframework.http.HttpStatus;
import subway.common.exception.BaseExceptionType;

public enum StationExceptionType implements BaseExceptionType {

    NOT_FOUND_STATION(
            100,
            HttpStatus.NOT_FOUND,
            "역이 존재하지 않습니다."
    ),
    DUPLICATE_STATION_NAME(
            101,
            HttpStatus.CONFLICT,
            "이미 등록된 역 이름입니다."
    ),
    ;

    private final int errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    StationExceptionType(final int errorCode, final HttpStatus httpStatus, final String errorMessage) {
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
