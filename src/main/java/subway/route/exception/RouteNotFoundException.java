package subway.route.exception;

public class RouteNotFoundException extends RuntimeException {

    public RouteNotFoundException(long sourceId, long destinationId) {
        super("경로를 찾을 수 없습니다 source id: " + sourceId + " destination id: " + destinationId);
    }
}
