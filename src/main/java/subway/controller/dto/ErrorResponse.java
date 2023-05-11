package subway.controller.dto;

public class ErrorResponse<T> {

    private final T data;

    public ErrorResponse(final T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
