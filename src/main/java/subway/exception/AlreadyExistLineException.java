package subway.exception;

public final class AlreadyExistLineException extends RuntimeException {

    public AlreadyExistLineException() {
        super("이미 존재하는 역입니다.");
    }
}
