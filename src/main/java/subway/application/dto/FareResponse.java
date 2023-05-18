package subway.application.dto;

public class FareResponse {

    private final int normalFare;
    private final int teenagerFare;
    private final int childFare;

    public FareResponse(final int normalFare, final int teenagerFare, final int childFare) {
        this.normalFare = normalFare;
        this.teenagerFare = teenagerFare;
        this.childFare = childFare;
    }

    public int getNormalFare() {
        return normalFare;
    }

    public int getTeenagerFare() {
        return teenagerFare;
    }

    public int getChildFare() {
        return childFare;
    }
}
