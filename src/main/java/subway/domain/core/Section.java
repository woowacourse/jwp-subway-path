package subway.domain.core;

import static subway.domain.core.Direction.LEFT;

import java.util.Objects;

public class Section {

    private final Long id;
    private final Station start;
    private final Station end;
    private final Distance distance;

    public Section(final String start, final String end, final int distance) {
        this(null, new Station(start), new Station(end), new Distance(distance));
    }

    public Section(final Station start, final Station end, final Distance distance) {
        this(null, start, end, distance);
    }

    public Section(final Long id, final String start, final String end, final int distance) {
        this(id, new Station(start), new Station(end), new Distance(distance));
    }

    public Section(final Long id, final Station start, final Station end, final Distance distance) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.distance = distance;
    }

    public boolean contains(final Station station) {
        return start.isSameName(station) || end.isSameName(station);
    }

    public boolean containsAll(final Station start, final Station end) {
        return (this.start.isSameName(start) && this.end.isSameName(end))
                || (this.start.isSameName(end) && this.end.isSameName(start));
    }

    public boolean moreThanOrEqual(final Distance distance) {
        return this.distance.moreThanOrEqual(distance);
    }

    public boolean isStationExistsAtDirection(final Station station, final Direction direction) {
        if (direction == LEFT) {
            return start.isSameName(station);
        }
        return end.isSameName(station);
    }

    public Distance subtract(final Distance distance) {
        return this.distance.subtract(distance);
    }

    public Distance add(final Distance distance) {
        return this.distance.add(distance);
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
        return Objects.equals(id, section.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", start=" + start +
                ", end=" + end +
                ", distance=" + distance +
                '}';
    }

    public Long getId() {
        return id;
    }

    public Station getStart() {
        return start;
    }

    public String getStartName() {
        return start.getName();
    }

    public Station getEnd() {
        return end;
    }

    public String getEndName() {
        return end.getName();
    }

    public Distance getDistance() {
        return distance;
    }

    public Integer getDistanceValue() {
        return distance.getValue();
    }
}
