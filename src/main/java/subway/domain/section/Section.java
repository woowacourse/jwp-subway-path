package subway.domain.section;

import java.util.Objects;
import subway.domain.station.Station;
import subway.domain.station.StationDistance;

public class Section {
    private final Station from;
    private final Station to;
    private final StationDistance distance;

    public Section(final Station from, final Station to, final StationDistance distance) {
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    public boolean matchFromStation(final Station from) {
        return this.from.equals(from);
    }

    public boolean matchToStation(final Station to) {
        return this.to.equals(to);
    }

    public Section attachFront(final Station station, final StationDistance stationDistance) {
        return new Section(station, from, stationDistance);
    }

    public Section attachBehind(final Station additionStation, final StationDistance stationDistance) {
        return new Section(to, additionStation, stationDistance);
    }

    public Station getFrom() {
        return from;
    }

    public Station getTo() {
        return to;
    }

    public StationDistance getDistance() {
        return distance;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Section section = (Section) o;
        return Objects.equals(getFrom(), section.getFrom()) && Objects.equals(getTo(), section.getTo())
                && Objects.equals(getDistance(), section.getDistance());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFrom(), getTo(), getDistance());
    }
}
