package subway.domain.exception;

public class DuplicateSectionException extends BusinessException {

    public DuplicateSectionException() {
        super("이미 존재하는 구간입니다");
    }
}
