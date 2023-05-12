package subway.section.domain;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import subway.station.domain.Station;
import subway.station.domain.StationDistance;

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

    public Section createFrontSection(final Station additionStation, final StationDistance stationDistance) {
        return new Section(additionStation, firstStation, stationDistance);
    }

    public Section createBehindSection(final Station additionStation, final StationDistance stationDistance) {
        return new Section(secondStation, additionStation, stationDistance);
    }

    public List<Section> separateByFirstStation(final Station additionStation,
                                                final StationDistance stationDistance) {
        final Section firstSection = new Section(firstStation, additionStation, stationDistance);
        final Section secondSection = new Section(additionStation, secondStation, distance.subtract(stationDistance));
        return List.of(firstSection, secondSection);
    }

    public boolean contains(final Station station) {
        return Set.of(firstStation, secondStation).contains(station);
    }

    public Section merge(final Section other) {
        final StationDistance sumDistance = this.distance.sum(other.distance);
        return new Section(firstStation, other.secondStation, sumDistance);
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
