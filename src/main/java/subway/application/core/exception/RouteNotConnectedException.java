package subway.application.core.exception;

public class RouteNotConnectedException extends ExpectedException {

    private static final String MESSAGE = "모든 노선이 이어지지 않았습니다.";

    public RouteNotConnectedException() {
        super(MESSAGE);
    }
}
