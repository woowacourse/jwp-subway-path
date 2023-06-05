package subway.line.domain;

import java.util.List;
import java.util.Objects;
import subway.interstation.domain.InterStation;
import subway.interstation.domain.InterStations;

public class Line {

    private final Long id;
    private final InterStations interStations;
    private LineName name;
    private LineColor color;

    public Line(Long id, LineColor color, LineName name, InterStations interStations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.interStations = interStations;
    }

    public Line(Long id, Line other) {
        this(id, other.color, other.name, other.interStations);
    }

    public Line(Long id, String name, String color, InterStations interStations) {
        this(id, new LineColor(color), new LineName(name), interStations);
    }

    public Line(Long id, String name, String color, List<InterStation> interStations) {
        this(id, name, color, new InterStations(interStations));
    }

    public Line(String name,
            String color,
            Long upStationId,
            Long downStationId,
            long distance) {
        this(null, name, color, InterStations.of(upStationId, downStationId, distance));
    }

    public void deleteStation(long existStationId) {
        interStations.remove(existStationId);
    }

    public void addInterStation(Long existStationId,
            Long downStationId,
            Long newStationId,
            long distance) {
        interStations.add(existStationId, downStationId, newStationId, distance);
    }

    public boolean isEmpty() {
        return interStations.isEmpty();
    }

    public void updateName(String name) {
        this.name = new LineName(name);
    }

    public void updateColor(String color) {
        this.color = new LineColor(color);
    }

    public Long getId() {
        return id;
    }

    public InterStations getInterStations() {
        return interStations;
    }

    public LineName getName() {
        return name;
    }

    public LineColor getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Line line = (Line) o;
        return Objects.equals(id, line.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
