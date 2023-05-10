package subway.domain.section;

import subway.domain.station.Station;

public class Section {

    private final Station upward;
    private final Station downward;
    private final Distance distance;

    public Section(final Station upward, final Station downward, final int distance) {
        this.upward = upward;
        this.downward = downward;
        this.distance = new Distance(distance);
    }

    public Station getUpward() {
        return upward;
    }

    public Station getDownward() {
        return downward;
    }

    public int getDistance() {
        return distance.getValue();
    }
}
