package subway.global.common;

public class ResultResponse {

    private int status;
    private String message;
    private Object data;

    public ResultResponse() {
    }

    public ResultResponse(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}
