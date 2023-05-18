package subway.domain.fee;

public class FeeInformation {

    private final int distance;

    public FeeInformation(int distance) {
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }
}
