package subway.exception;

public class EmptyNameException extends SubwayException {
    public EmptyNameException() {
        super("아름에는 빈 문자가 들어올 수 없습니다.");
    }
}
