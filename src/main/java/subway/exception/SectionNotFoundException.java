package subway.exception;

public class SectionNotFoundException extends RuntimeException {

	public SectionNotFoundException() {
		super("Section을 찾을 수 없습니다");
	}
}
