package subway.exception;

import java.time.LocalDateTime;

public class ErrorResponse {

    private final String message;
    private final String status;
    private final LocalDateTime time;

    private ErrorResponse(final String message, final String status, final LocalDateTime time) {
        this.message = message;
        this.status = status;
        this.time = time;
    }

    public static ErrorResponse from(final String message, final String status, final LocalDateTime time) {
        return new ErrorResponse(message, status, time);
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getTime() {
        return time;
    }
}
