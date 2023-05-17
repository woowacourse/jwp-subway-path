package subway.exception;

public class AlreadyExistSectionException extends SubwayException {
    public AlreadyExistSectionException() {
        super("이미 존재하는 구간입니다.");
    }
}
