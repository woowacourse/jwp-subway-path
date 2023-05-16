package subway.exception;

public final class AlreadyExistStationException extends RuntimeException {

    public AlreadyExistStationException() {
        super("이미 존재하는 역입니다.");
    }
}
