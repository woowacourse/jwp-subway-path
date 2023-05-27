package subway.exception;

public class InvalidSectionDistanceException extends RuntimeException {

	public InvalidSectionDistanceException() {
		super("구간의 거리는 기존의 거리보다 크면 예외가 발생한다");
	}
}
