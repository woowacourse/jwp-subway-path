package subway.domain.section;

import subway.domain.station.Station;

public class Section {

    private final Station source;
    private final Station target;
    private final SectionDistance distance;

    public Section(final Station source, final Station target, final int distance) {
        this.source = source;
        this.target = target;
        this.distance = new SectionDistance(distance);
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }

    public SectionDistance getDistance() {
        return distance;
    }
}
