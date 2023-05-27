package subway.exception;

public class SameSectionException extends RuntimeException {

	public SameSectionException() {
		super("이미 존재하는 구간입니다");
	}
}
