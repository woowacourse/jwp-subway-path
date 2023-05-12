package subway.exception;

public class NameIsBlankException extends RuntimeException {

    public NameIsBlankException() {
        super("노선 혹은 역의 이름을 입력해주세요.");
    }
}
