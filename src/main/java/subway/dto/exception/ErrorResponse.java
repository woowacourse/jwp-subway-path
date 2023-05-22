package subway.dto.exception;

public class ErrorResponse {

	private final String message;
	private final int statusCode;

	private ErrorResponse(final String message, final int statusCode) {
		this.message = message;
		this.statusCode = statusCode;
	}

	public static ErrorResponse from(final String message, final int statusCode) {
		return new ErrorResponse(message, statusCode);
	}

	public String getMessage() {
		return message;
	}

	public int getStatusCode() {
		return statusCode;
	}
}
