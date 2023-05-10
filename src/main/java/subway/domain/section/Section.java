package subway.domain.section;

import java.util.Objects;
import subway.domain.station.Station;
import subway.domain.station.StationDistance;

public class Section {
    private final Station firstStation;
    private final Station secondStation;
    private final StationDistance distance;

    public Section(final Station firstStation, final Station secondStation, final StationDistance distance) {
        this.firstStation = firstStation;
        this.secondStation = secondStation;
        this.distance = distance;
    }

    public boolean matchFirstStation(final Station firstStation) {
        return this.firstStation.equals(firstStation);
    }

    public boolean matchSecondStation(final Station secondStation) {
        return this.secondStation.equals(secondStation);
    }

    public Section attachFront(final Station additionStation, final StationDistance stationDistance) {
        return new Section(additionStation, firstStation, stationDistance);
    }

    public Section attachBehind(final Station additionStation, final StationDistance stationDistance) {
        return new Section(secondStation, additionStation, stationDistance);
    }

    public Station getFirstStation() {
        return firstStation;
    }

    public Station getSecondStation() {
        return secondStation;
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
        return Objects.equals(getFirstStation(), section.getFirstStation()) && Objects.equals(getSecondStation(),
                section.getSecondStation())
                && Objects.equals(getDistance(), section.getDistance());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstStation(), getSecondStation(), getDistance());
    }
}
