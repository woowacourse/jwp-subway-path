package subway.domain.section;

import subway.domain.station.Station;

public class Section {

    private final Station upward;
    private final Station downward;
    private final Distance distance;

    private Section(final Station upward, final Station downward, final int distance) {
        this.upward = upward;
        this.downward = downward;
        this.distance = new Distance(distance);
    }
}
