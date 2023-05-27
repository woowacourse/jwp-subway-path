package subway.exception;

public class InvalidShortestPathException extends IllegalArgumentException {

    private static final String MESSAGE = "잘못된 방향입니다.";

    public InvalidShortestPathException() {
        super(MESSAGE);
    }
}
