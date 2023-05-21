package subway.exception;

public class DuplicateSectionException extends RuntimeException {

    public DuplicateSectionException() {
        super("이미 포함되어 있는 구간입니다.");
    }
}
