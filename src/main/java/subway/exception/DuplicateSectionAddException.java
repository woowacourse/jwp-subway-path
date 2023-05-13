package subway.exception;

public class DuplicateSectionAddException extends RuntimeException {
    public DuplicateSectionAddException() {
        super("이미 존재하는 구간입니다.");
    }
}
