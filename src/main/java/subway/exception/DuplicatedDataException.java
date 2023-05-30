package subway.exception;

public class DuplicatedDataException extends RuntimeException {

    public DuplicatedDataException() {
        super("이미 해당 데이터가 존재합니다.");
    }

    public DuplicatedDataException(final String message) {
        super(message);
    }
}
