package subway.exception;

public class DuplicateSectionException extends GlobalException {
    public DuplicateSectionException() {
        super("구간은 중복될 수 없습니다.");
    }
}
