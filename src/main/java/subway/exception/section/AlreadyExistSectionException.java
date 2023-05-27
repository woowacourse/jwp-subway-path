package subway.exception.section;

public class AlreadyExistSectionException extends SectionException {
    public AlreadyExistSectionException() {
        super("이미 존재하는 구간입니다.");
    }
}
