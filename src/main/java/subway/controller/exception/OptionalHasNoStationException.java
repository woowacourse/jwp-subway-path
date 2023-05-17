package subway.controller.exception;

public class OptionalHasNoStationException extends RuntimeException {

    public OptionalHasNoStationException() {
        super("[ERROR] 해당하는 역이 존재하지 않습니다.");
    }
}
