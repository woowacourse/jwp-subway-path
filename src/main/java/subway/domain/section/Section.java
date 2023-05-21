package subway.domain.section;

import subway.domain.station.Station;

import java.util.Objects;

public class Section {

    private final Long id;
    private final Station from;
    private final Station to;
    private final Distance distance;

    public Section(final Long id, final Station from, final Station to, final int distance) {
        validateDuplication(from, to);
        this.id = id;
        this.from = from;
        this.to = to;
        this.distance = new Distance(distance);
    }

    public Section(final Station from, final Station to, final int distance) {
        this(null, from, to, distance);
    }

    private void validateDuplication(final Station from, final Station to) {
        if (from.equals(to)) {
            throw new IllegalArgumentException("동일한 역을 추가할 수 없습니다.");
        }
    }

    public boolean contains(final Station station) {
        return containsOnLeft(station) || containsOnRight(station);
    }

    public boolean containsOnLeft(final Station station) {
        return station.equals(from);
    }

    public boolean containsOnRight(final Station station) {
        return station.equals(to);
    }

    public boolean isInsertable(final int distance) {
        return this.distance.isLongerThan(distance);
    }

    public Section changeLeft(Station station, int distance) {
        return new Section(station, to, this.distance.subtract(distance));
    }

    public Section changeRight(final Station station, final int distance) {
        return new Section(from, station, this.distance.subtract(distance));
    }

    public Long getId() {
        return id;
    }

    public Station getFrom() {
        return from;
    }

    public Station getTo() {
        return to;
    }

    public Distance getDistance() {
        return distance;
    }

    public int getDistanceValue() {
        return distance.getDistance();
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
        return Objects.equals(from, section.from) && Objects.equals(to, section.to) && Objects.equals(distance, section.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, distance);
    }

    @Override
    public String toString() {
        return "Section{" +
                "from=" + from +
                ", to=" + to +
                ", distance=" + distance +
                '}';
    }
}
