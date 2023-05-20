package subway.exception;

import java.time.LocalDateTime;

public class ErrorResponse {
	private final String message;
	private final LocalDateTime time;
	private final int statusCode;

	public ErrorResponse(final String message, final LocalDateTime time, final int statusCode) {
		this.message = message;
		this.time = time;
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public int getStatusCode() {
		return statusCode;
	}
}
