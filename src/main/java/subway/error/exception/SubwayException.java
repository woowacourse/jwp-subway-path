package subway.error.exception;

public class SubwayException extends RuntimeException {
	private final ErrorCode errorCode;

	public SubwayException(final ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}
}
