package subway.exception;

public class SectionNotConnectException extends RuntimeException {

    public SectionNotConnectException() {
        super("해당하는 호선이 없기 때문에 Section을 연결할 수 없습니다.");
    }
}
