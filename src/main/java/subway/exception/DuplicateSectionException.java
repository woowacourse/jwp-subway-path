package subway.exception;

public class DuplicateSectionException extends SubwayException {

    public DuplicateSectionException() {
        super("이미 포함되어 있는 구간입니다.");
    }
}
