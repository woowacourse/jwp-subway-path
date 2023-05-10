package subway.domain;

import java.util.Objects;

public class Section {

    private final Long id;
    private final Station source;
    private final Station target;
    private final Distance distance;

    public Section(Station source, Station target, int distance) {
        this(null, source, target, new Distance(distance));
    }

    public Section(String source, String target, int distance) {
        this(new Station(source), new Station(target), distance);
    }

    public Section(Long id, Station source, Station target, int distance) {
        this(null, source, target, new Distance(distance));
    }

    public Section(Long id, Station source, Station target, Distance distance) {
        validateTarget(source, target);
        this.id = id;
        this.source = source;
        this.target = target;
        this.distance = distance;
    }

    private void validateTarget(Station source, Station target) {
        if (source.isSameName(target)) {
            throw new IllegalArgumentException("출발지와 도착지는 같을 수 없습니다");
        }
    }

    public boolean isAnySame(Station station) {
        return source.isSameName(station) || target.isSameName(station);
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }

    public int getDistance() {
        return distance.getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Section section = (Section) o;
        return Objects.equals(source, section.source) && Objects.equals(target, section.target)
                && Objects.equals(distance, section.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target, distance);
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", source=" + source +
                ", target=" + target +
                ", distance=" + distance +
                '}';
    }
}
