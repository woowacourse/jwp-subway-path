package subway.error.exception;

public class ErrorCode {

	private final int status;
	private final String code;
	private final String message;

	public ErrorCode(final int status, final String code, final String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
