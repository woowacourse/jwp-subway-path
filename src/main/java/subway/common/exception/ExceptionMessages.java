package subway.common.exception;

public class ExceptionMessages {
    private ExceptionMessages() {
    }

    public static final String STRATEGY_MAPPING_FAILED = "Strategy 매핑이 제대로 되지 않았습니다.";
    public static final String INVALID_DISTANCE = "거리 정보는 양의 정수로 제한합니다.";
    public static final String STATION_NOT_REGISTERED = "등록하는 역과 연결되는 기존의 역 정보가 노선상에 존재하지 않습니다.";
    public static final String STATION_NOT_CONNECTED = "주어진 두 역이 이웃한 역이 아닙니다.";
}
