package subway.exception;

public enum ErrorCode {
    INVALID_REQUEST(""),
    LINE_NOT_FOUND("노선 정보가 존재하지 않습니다."),
    LINE_NAME_DUPLICATED("노선 이름은 중복될 수 없습니다."),
    LINE_NAME_LENGTH("노선 이름 길이는 3~10여야 합니다."),
    LINE_EXTRA_FARE_RANGE("추가 요금은 음수일 수 없습니다."),
    DB_UPDATE_ERROR("DB 업데이트가 정상적으로 진행되지 않았습니다."),
    DB_DELETE_ERROR("DB 삭제가 정상적으로 진행되지 않았습니다."),
    STATION_NOT_FOUND("역 정보가 존재하지 않습니다."),
    STATION_NAME_DUPLICATED("역 이름은 중복될 수 없습니다"),
    STATION_NAME_LENGTH("역 이름 길이는 3~10여야 합니다."),
    SECTION_DISTANCE("거리는 최소 1부터 최대 50까지 가능합니다."),
    SECTION_ADD_STATION_NOT_EXISTS("존재하지 않는 역을 추가할 수 없습니다."),
    SECTION_ALREADY_ADD("이미 추가된 구간입니다."),
    SECTION_TOO_FAR_DISTANCE("거리가 너무 커서 역을 추가할 수 없습니다."),
    ROUTE_NOT_EXISTS("목적지로 이동할 수 없습니다."),
    INTERNAL_SERVER_ERROR("서버에서 예기치 못한 오류가 발생하였습니다.");

    private final String message;

    ErrorCode(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
