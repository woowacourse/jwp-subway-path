package subway.exception;

public class SectionSeparatedException extends RuntimeException {

    public SectionSeparatedException() {
        super("갈래길은 생길 수 없습니다. Section을 다시 확인해주세요.");
    }
}
