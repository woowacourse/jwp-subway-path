package subway.application.core.exception;

public class SectionConnectException extends ExpectedException {

    private static final String MESSAGE = "두 섹션은 접합부가 달라 연결될 수 없습니다.";

    public SectionConnectException() {
        super(MESSAGE);
    }
}
