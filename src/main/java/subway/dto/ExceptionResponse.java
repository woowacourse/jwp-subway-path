package subway.dto;

import java.util.Objects;

public class ExceptionResponse {
    private final String message;
    
    public ExceptionResponse(final String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ExceptionResponse that = (ExceptionResponse) o;
        return Objects.equals(message, that.message);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(message);
    }
    
    @Override
    public String toString() {
        return "ExceptionResponse{" +
                "message='" + message + '\'' +
                '}';
    }
}
