package subway.exception;

public class SectionDuplicatedException extends RuntimeException {

    public SectionDuplicatedException() {
        super("중복된 Section입니다. 다시 확인해주세요.");
    }
}
