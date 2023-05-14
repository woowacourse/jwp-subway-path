package subway.exception;

public class ColorNotBlankException extends RuntimeException {

    public ColorNotBlankException() {
        super("노선의 색상을 입력해주세요.");
    }
}
