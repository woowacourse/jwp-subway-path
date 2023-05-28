package subway.dto;

import java.util.Objects;

public class ExceptionResponse {

    private final Integer code;
    private final String status;
    private final String message;

    public ExceptionResponse(final Integer code, final String status, final String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ExceptionResponse that = (ExceptionResponse) o;
        return Objects.equals(code, that.code)
                && Objects.equals(status, that.status)
                && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, status, message);
    }

    @Override
    public String toString() {
        return "ExceptionResponse{" +
                "code=" + code +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
