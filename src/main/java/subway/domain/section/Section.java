package subway.domain.section;

import subway.domain.line.Line;
import subway.domain.station.Station;

import java.util.Objects;

public class Section {

    private final Long id;
    private final NearbyStations nearbyStations;
    private final Line line;
    private final Distance distance;

    public Section(Long id, NearbyStations nearbyStations, Line line,
                   Distance distance) {
        this.id = id;
        this.nearbyStations = nearbyStations;
        this.line = line;
        this.distance = distance;
    }

    public Station getUpStation() {
        return nearbyStations.getUpStation();
    }

    public Station getDownStation() {
        return nearbyStations.getDownStation();
    }

    public Long getUpStationId() {
        return nearbyStations.getUpStation().getId();
    }

    public Long getDownStationId() {
        return nearbyStations.getDownStation().getId();
    }

    public Long getId() {
        return id;
    }

    public NearbyStations getNearbyStations() {
        return nearbyStations;
    }

    public Line getLine() {
        return line;
    }

    public Distance getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return Objects.equals(id, section.id) && Objects.equals(nearbyStations, section.nearbyStations) && Objects.equals(line, section.line) && Objects.equals(distance, section.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nearbyStations, line, distance);
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", adjacentStations=" + nearbyStations +
                ", line=" + line +
                ", distance=" + distance +
                '}';
    }
}
