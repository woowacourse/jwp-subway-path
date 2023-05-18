package subway.exception;

public class StationNotFoundException extends RuntimeException {

    public StationNotFoundException() {
        super("입력된 역을 찾을 수 없습니다.");
    }

}