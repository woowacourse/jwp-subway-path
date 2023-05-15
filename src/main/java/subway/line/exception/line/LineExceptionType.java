package subway.line.exception.line;

import org.springframework.http.HttpStatus;
import subway.common.exception.BaseExceptionType;

public enum LineExceptionType implements BaseExceptionType {

    NOT_FOUND_LINE(
            200,
            HttpStatus.NOT_FOUND,
            "노선이 존재하지 않습니다."
    ),
    UP_AND_DOWN_STATION_IS_SAME(
            201,
            HttpStatus.BAD_REQUEST,
            "노선은 상행역과 하행역이 동일해서는 안됩니다"
    ),
    DUPLICATE_LINE_NAME(
            202,
            HttpStatus.CONFLICT,
            "이미 등록된 노선 이름입니다."
    ),
    ALREADY_EXIST_STATIONS(
            203,
            HttpStatus.BAD_REQUEST,
            "추가하려는 두 역이 이미 포함되어 있습니다."
    ),
    DELETED_STATION_NOT_EXIST(
            204,
            HttpStatus.BAD_REQUEST,
            "없는 역은 제거할 수 없습니다."
    ),
    NON_POSITIVE_DISTANCE(
            205,
            HttpStatus.BAD_REQUEST,
            "역간 거리는 양수여야 합니다."
    ),
    NO_RELATION_WITH_ADDED_SECTION(
            206,
            HttpStatus.BAD_REQUEST,
            "해당 구간은 기존 노선과 연관관계가 없어 포함될 수 없습니다."
    ),
    ADDED_SECTION_NOT_SMALLER_THAN_ORIGIN(
            207,
            HttpStatus.BAD_REQUEST,
            "추가될 구간은 기존 구간의 길이보다 작아야 합니다."
    ),
    INCONSISTENT_EXISTING_SECTION(
            208,
            HttpStatus.BAD_REQUEST,
            "기존에 등록된 구간과 불일치합니다."
    ),
    ABNORMAL_EXCEPTION(
            299,
            HttpStatus.INTERNAL_SERVER_ERROR,
            "발생하면 안되는 예외 발생 (설명 : %s)"
    ),
    ;

    private final int errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    LineExceptionType(final int errorCode, final HttpStatus httpStatus, final String errorMessage) {
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
