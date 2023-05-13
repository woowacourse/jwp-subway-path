package subway.domain;

import java.util.Objects;

public class Section {
    private final int distance;
    private final Station departure;
    private final Station arrival;
    private Line line;

    public Section(int distance, Station departure, Station arrival, Line line) {
        this.distance = distance;
        this.departure = departure;
        this.arrival = arrival;
        this.line = line;
    }

    public Section getReverse() {
        return new Section(distance, arrival, departure, line);
    }

    public boolean LineEquals(Section other) {
        return line.equals(other.getLine());
    }

    public boolean isDeparture(Station station) {
        return departure.equals(station);
    }

    public boolean isArrival(Station station) {
        return arrival.getName().equals(station.getName());
    }

    public int getDistance() {
        return distance;
    }

    public Station getDeparture() {
        return departure;
    }

    public Station getArrival() {
        return arrival;
    }

    public Line getLine() {
        return line;
    }

    public void setLineId(Long id) {
        line.setId(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return distance == section.distance && Objects.equals(departure, section.departure) && Objects.equals(arrival, section.arrival) && Objects.equals(line, section.line);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance, departure, arrival, line);
    }
}
