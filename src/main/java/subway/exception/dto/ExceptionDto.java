package subway.exception.dto;

public class ExceptionDto {

    private final String message;

    public ExceptionDto(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
