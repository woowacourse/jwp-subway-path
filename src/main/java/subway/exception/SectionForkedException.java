package subway.exception;

public class SectionForkedException extends RuntimeException {

    public SectionForkedException() {
        super("구간 갈랫길이 생기면 안됩니다.");
    }
}
