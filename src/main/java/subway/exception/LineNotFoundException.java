package subway.exception;

public class LineNotFoundException extends RuntimeException {

	public LineNotFoundException() {
		super("해당하는 노선을 찾을 수 없습니다");
	}
}
