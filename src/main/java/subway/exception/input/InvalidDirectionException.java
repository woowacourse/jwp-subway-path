package subway.exception.input;

public class InvalidDirectionException extends InputException {

    private final static String INVALID_DIRECTION_MESSAGE = "잘못된 방향입니다.";

    public InvalidDirectionException() {
        super(INVALID_DIRECTION_MESSAGE);
    }
}
