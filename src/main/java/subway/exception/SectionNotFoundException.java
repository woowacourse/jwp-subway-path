package subway.exception;

public class SectionNotFoundException extends RuntimeException {

    private static final String MESSAGE = "구간이 존재하지 않습니다.";

    public SectionNotFoundException() {
        super(MESSAGE);
    }
}
