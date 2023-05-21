package subway.exception;

public class IllegalLineNameException extends RuntimeException {

    public IllegalLineNameException() {
        super("호선 이름은 10자 이하여야 합니다.");
    }
}
