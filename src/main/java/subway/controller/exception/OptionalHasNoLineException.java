package subway.controller.exception;

public class OptionalHasNoLineException extends RuntimeException {

    public OptionalHasNoLineException() {
        super("[ERROR] 해당하는 노선이 존재하지 않습니다.");
    }
}
