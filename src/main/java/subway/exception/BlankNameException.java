package subway.exception;

public class BlankNameException extends RuntimeException {

	public BlankNameException() {
		super("노선 혹은 역의 이름을 입력해주세요.");
	}
}
