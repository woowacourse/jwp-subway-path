package subway.route.exception;

public class IdenticalStationsException extends RuntimeException {

    public IdenticalStationsException(Long sourceId, Long destinationId) {
        super("시작역과 도착역이 동일합니다 source id: " + sourceId + " destination id: " + destinationId);
    }
}
