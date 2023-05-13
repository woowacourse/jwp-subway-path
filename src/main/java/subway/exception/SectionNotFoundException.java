package subway.exception;

public class SectionNotFoundException extends NotFoundException {

    public SectionNotFoundException() {
        super("역 구간 정보를 찾을 수 없습니다.");
    }
}
