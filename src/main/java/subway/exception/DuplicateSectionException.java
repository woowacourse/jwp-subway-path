package subway.exception;

public class DuplicateSectionException extends RuntimeException {
    public DuplicateSectionException() {
        super("구간은 중복될 수 없습니다.");
    }
}
