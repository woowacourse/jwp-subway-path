package subway.exception;

public class DuplicatedNameException extends RuntimeException {
    public DuplicatedNameException() {
        super("중복된 이름으로 만들 수 없습니다.");
    }
}
