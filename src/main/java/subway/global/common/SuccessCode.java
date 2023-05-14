package subway.global.common;

public enum SuccessCode {

    CREATE_LINE(201, "노선 추가 성공"),
    UPDATE_LINE(200, "노선 업데이트 성공"),
    DELETE_LINE(204, "노선 삭제 성공"),
    SELECT_STATIONS(200,"전체 역 조회 성공"),
    SELECT_STATION(200,"단일 역 조회 성공"),
    CREATE_STATION(201, "역 추가 성공"),
    UPDATE_STATION(200, "역 업데이트 성공"),
    DELETE_STATION(204, "역 삭제 성공"),
    CREATE_SECTION(201, "구간 추가 성공"),
    DELETE_SECTION(204, "구간 삭제 성공"),
    SELECT_SUBWAY_MAP(200,"단일 노선도 조회 성공"),
    SELECT_SUBWAY_MAPS(200, "전체 노선도 조회 성공");


    private final int status;
    private final String message;

    SuccessCode(final int status, final String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
