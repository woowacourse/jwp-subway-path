package subway.exception;

public class DuplicateStationException extends RuntimeException {

    public DuplicateStationException() {
        super("이미 존재하는 역입니다.");
    }
}
