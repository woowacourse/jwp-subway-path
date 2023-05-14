package subway.exception;

public class NoSuchSectionException extends ApiNoSuchResourceException {

    public NoSuchSectionException() {
        super("존재하지 않는 구간입니다.");
    }

    public NoSuchSectionException(Long sectionId) {
        super("존재하지 않는 구간입니다. sectionId : " + sectionId);
    }
}
