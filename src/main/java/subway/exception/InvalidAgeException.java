package subway.exception;

public class InvalidAgeException extends SubwayException {

    public InvalidAgeException() {
        super("올바른 나이가 아닙니다.");
    }
}
