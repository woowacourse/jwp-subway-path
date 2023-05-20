package subway.path.application.dto;

public class FeeInfo {

    private final String info;
    private final int fee;

    public FeeInfo(final String info, final int fee) {
        this.info = info;
        this.fee = fee;
    }

    public String getInfo() {
        return info;
    }

    public int getFee() {
        return fee;
    }
}
