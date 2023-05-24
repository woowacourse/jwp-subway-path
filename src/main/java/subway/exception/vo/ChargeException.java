package subway.exception.vo;

public class ChargeException extends VoException {
    private static final String MESSAGE = "금액은 음수가 될 수 없습니다.";

    public ChargeException() {
        super(MESSAGE);
    }
}
