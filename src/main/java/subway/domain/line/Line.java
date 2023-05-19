package subway.domain.line;

import java.util.List;
import java.util.Objects;
import subway.domain.interstation.InterStation;
import subway.domain.interstation.InterStations;

public class Line {

    private final Long id;
    private final InterStations interStations;
    private LineName name;
    private LineColor color;

    public Line(final Long id, final LineColor color, final LineName name, final InterStations interStations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.interStations = interStations;
    }

    public Line(final Long id, final Line other) {
        this(id, other.color, other.name, other.interStations);
    }

    public Line(final Long id, final String name, final String color, final InterStations interStations) {
        this(id, new LineColor(color), new LineName(name), interStations);
    }

    public Line(final Long id, final String name, final String color, final List<InterStation> interStations) {
        this(id, name, color, new InterStations(interStations));
    }

    public Line(final String name,
            final String color,
            final Long upStationId,
            final Long downStationId,
            final long distance) {
        this(null, name, color, InterStations.of(upStationId, downStationId, distance));
    }

    public void deleteStation(final long existStationId) {
        interStations.remove(existStationId);
    }

    public void addInterStation(final Long existStationId,
            final Long downStationId,
            final Long newStationId,
            final long distance) {
        interStations.add(existStationId, downStationId, newStationId, distance);
    }

    public boolean isEmpty() {
        return interStations.isEmpty();
    }

    public void updateName(final String name) {
        this.name = new LineName(name);
    }

    public void updateColor(final String color) {
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
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Line line = (Line) o;
        return Objects.equals(id, line.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
