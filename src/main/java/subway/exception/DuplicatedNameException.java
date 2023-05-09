package subway.exception;

public class DuplicatedNameException extends RuntimeException {

    public DuplicatedNameException(String input) {
        super("이미 존재하는 이름입니다 입력값 : " + input);
    }
}
