package subway.exception;

public class StationAlreadyExistsException extends RuntimeException {

    public StationAlreadyExistsException() {
        super("노선에 이미 존재하는 역입니다.");
    }

}
