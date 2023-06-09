package subway.common.exception;

public class ErrorResponse {

    private final String data;

    public ErrorResponse(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }
}
