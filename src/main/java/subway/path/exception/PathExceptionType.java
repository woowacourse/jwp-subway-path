package subway.path.exception;

import org.springframework.http.HttpStatus;
import subway.common.exception.BaseExceptionType;

public enum PathExceptionType implements BaseExceptionType {

    NOT_EXIST_STATION_IN_LINES(
            300,
            HttpStatus.BAD_REQUEST,
            "출발역 혹은 종착역이 경로상에 존재하지 않습니다"
    ),
    START_AND_END_STATIONS_IS_SAME(
            301,
            HttpStatus.BAD_REQUEST,
            "경로를 구할 때에는 출발역과 종착역을 다르게 설정해주세요"
    ),
    NO_PATH(
            302,
            HttpStatus.BAD_REQUEST,
            "출발역에서 종착역으로 이어지는 경로가 없습니다"
    ),
    ABNORMAL_EXCEPTION(
            399,
            HttpStatus.INTERNAL_SERVER_ERROR,
            "발생하면 안되는 예외 발생 (설명 : %s)"
    ),
    ;

    private final int errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    PathExceptionType(final int errorCode, final HttpStatus httpStatus, final String errorMessage) {
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
