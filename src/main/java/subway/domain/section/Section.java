package subway.domain.section;

import subway.domain.station.Station;
import subway.entity.SectionEntity;

public final class Section {

    private final Station upward;
    private final Station downward;
    private final Distance distance;

    public Section(final Station upward, final Station downward, final int distance) {
        this.upward = upward;
        this.downward = downward;
        this.distance = new Distance(distance);
    }

    public static Section from(final SectionEntity sectionEntity) {
        final Station upward = new Station(sectionEntity.getUpwardStationId(), sectionEntity.getUpwardStation());
        final Station downward = new Station(sectionEntity.getDownwardStationId(), sectionEntity.getDownwardStation());
        return new Section(upward, downward, sectionEntity.getDistance());
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
