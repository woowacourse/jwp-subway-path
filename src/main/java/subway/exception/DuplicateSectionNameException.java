package subway.exception;

public class DuplicateSectionNameException extends GlobalException {
    public DuplicateSectionNameException() {
        super("시작 역과 도착 역은 같을 수 없습니다.");
    }
}
