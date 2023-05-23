package subway.domain.section;

import subway.domain.station.Station;

import java.util.Objects;

public class Section {
    private final Station beforeStation;
    private final Station nextStation;
    private final Distance distance;
    private Long id;

    public Section(final Long id, final Station beforeStation, final Station nextStation, final Distance distance) {
        this(beforeStation, nextStation, distance);
        this.id = id;
    }

    public Section(final Station beforeStation, final Station nextStation, final Distance distance) {
        this.beforeStation = beforeStation;
        this.nextStation = nextStation;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Station getBeforeStation() {
        return beforeStation;
    }

    public Station getNextStation() {
        return nextStation;
    }

    public Distance getDistance() {
        return distance;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Section section = (Section) o;
        return Objects.equals(id, section.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
