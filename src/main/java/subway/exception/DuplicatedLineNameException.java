package subway.exception;

public class DuplicatedLineNameException extends IllegalArgumentException {

    private static final String MESSAGE = "이미 존재하는 노선입니다.";

    public DuplicatedLineNameException() {
        super(MESSAGE);
    }
}
