package subway.domain.section;

import subway.domain.line.Line;
import subway.domain.station.Station;

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

    public boolean isSameDownStationName(String downStationName) {
        return nearbyStations.getDownStation().isSameStationName(downStationName);
    }

    public boolean isSameUpStationName(String upStationName) {
        return nearbyStations.getUpStation().isSameStationName(upStationName);
    }

    public boolean isSameLineId(Long lineId) {
        return line.getId().equals(lineId);
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
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", nearbyStations=" + nearbyStations +
                ", line=" + line +
                ", distance=" + distance +
                '}';
    }
}
