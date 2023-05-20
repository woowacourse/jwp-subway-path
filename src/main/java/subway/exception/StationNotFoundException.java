package subway.exception;

public class StationNotFoundException extends IllegalArgumentException {

    private static final String MESSAGE = "존재하지 않는 역입니다.";

    public StationNotFoundException() {
        super(MESSAGE);
    }

}
