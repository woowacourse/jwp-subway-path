package subway.exception;

public class IllegalInputForDomainException extends RuntimeException {

    public IllegalInputForDomainException() {
        super("잘못된 값입니다.");
    }

    public IllegalInputForDomainException(final String message) {
        super(message);
    }
}
