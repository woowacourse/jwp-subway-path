package subway.dto.response;

import java.util.List;

public class ExceptionResponse {

    private final List<String> messages;

    public ExceptionResponse(List<String> messages) {
        this.messages = messages;
    }

    public List<String> getMessages() {
        return messages;
    }
}
