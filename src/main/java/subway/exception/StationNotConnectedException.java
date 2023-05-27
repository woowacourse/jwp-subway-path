package subway.exception;

public class StationNotConnectedException extends RuntimeException {

	public StationNotConnectedException() {
		super("겹치는 역이 없어 Section을 연결할 수 없습니다");
	}
}
