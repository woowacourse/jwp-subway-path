package subway.domain;

public class Section {
    private final Station upwardStation;
    private final Station downwardStation;
    private final int distance;

    public Section(Station upwardStation, Station downwardStation, int distance) {
        this.upwardStation = upwardStation;
        this.downwardStation = downwardStation;
        this.distance = distance;
    }


    public int calculateRemainingDistance(int distanceToSubtract) {
        return this.distance - distanceToSubtract;
    }
}
