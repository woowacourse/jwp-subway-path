package subway.exception;

public class InvalidSectionException extends GlobalException {
    public InvalidSectionException() {
        super("존재하지 않는 구간입니다.");
    }
}
