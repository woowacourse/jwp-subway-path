package subway.application.core.exception;

public class StationTooFarException extends ExpectedException {

    private static final String MESSAGE = "신규로 등록된 역이 기존 노선의 거리 범위를 벗어날 수 없습니다.";

    public StationTooFarException() {
        super(MESSAGE);
    }
}
