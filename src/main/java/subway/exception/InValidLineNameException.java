package subway.exception;

public class InValidLineNameException extends RuntimeException {
    public InValidLineNameException() {
        super("호선은 이름은 1글자 이상, 10글자 이하여야 한다.");
    }
}
