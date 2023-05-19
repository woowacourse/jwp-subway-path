package subway.exception;

public class NotExistStationException extends GlobalException {
    public NotExistStationException() {
        super("두 역을 동시에 등록해야합니다.");
    }
}
