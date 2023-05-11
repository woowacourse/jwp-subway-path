package subway.dto;

import java.util.List;

public class ExceptionResponse {

    private final List<String> message;

    public ExceptionResponse(List<String> message) {
        this.message = message;
    }

    public List<String> getMessage() {
        return message;
    }
}
