package subway.exception;

public class DuplicatedStationNameException extends IllegalArgumentException {

    private static final String MESSAGE = "이미 존재하는 역입니다.";

    public DuplicatedStationNameException() {
        super(MESSAGE);
    }
}
