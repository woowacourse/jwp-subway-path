package subway.exception;

public class InvalidDirectionException extends IllegalArgumentException {

    private static final String MESSAGE = "잘못된 방향입니다.";

    public InvalidDirectionException() {
        super(MESSAGE);
    }
}
