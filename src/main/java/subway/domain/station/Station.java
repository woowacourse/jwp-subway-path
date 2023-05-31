package subway.domain.station;

import subway.domain.line.Line;

import java.util.Objects;

public class Station {

    private final Long id;
    private final StationName name;
    private final Line line;

    public Station(Long id, String name, Line line) {
        this.id = id;
        this.name = new StationName(name);
        this.line = line;
    }

    public boolean isSameStation(Station otherStation) {
        return this.getLineName().equals(otherStation.getLineName()) &&
                this.getName().equals(otherStation.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getStationName();
    }

    public Line getLine() {
        return line;
    }

    public Long getLineId() {
        return line.getId();
    }

    public String getLineName() {
        return line.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Station station = (Station) o;
        return Objects.equals(id, station.id) && Objects.equals(name, station.name) && Objects.equals(line, station.line);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, line);
    }

    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", name=" + name +
                ", line=" + line +
                '}';
    }
}
