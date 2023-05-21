package subway.exception;

public class DistanceLessThatOneException extends RuntimeException {

	public DistanceLessThatOneException() {
		super("역 사이의 거리는 1 이상이 되어야합니다");
	}
}
