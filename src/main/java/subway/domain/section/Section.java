package subway.domain.section;

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

    public Section attachFront(final Station station, final StationDistance stationDistance) {
        return new Section(station, from, stationDistance);
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
}
