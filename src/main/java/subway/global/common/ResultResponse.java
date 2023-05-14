package subway.global.common;

public class ResultResponse {

    private int status;
    private String message;
    private Object data;

    public ResultResponse() {
    }

    public ResultResponse(SuccessCode resultCode, Object data) {
        this.status = resultCode.getStatus();
        this.message = resultCode.getMessage();
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
