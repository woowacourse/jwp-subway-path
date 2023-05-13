package subway.exception.invalid;

public class SectionInvalidException extends InvalidException {

    public SectionInvalidException() {
        super("유효하지 않은 역 구간 정보입니다.");
    }
}
