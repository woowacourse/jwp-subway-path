package subway.domain;

import java.util.Objects;
import subway.exception.section.InvalidSectionDirectionException;

public class Section {

    private final Station source;
    private final Station target;
    private final Distance distance;

    public Section(String source, String target, int distance) {
        this(new Station(source), new Station(target), new Distance(distance));
    }

    public Section(Station source, Station target, int distance) {
        this(source, target, new Distance(distance));
    }

    public Section(Station source, Station target, Distance distance) {
        validateTarget(source, target);
        this.source = source;
        this.target = target;
        this.distance = distance;
    }

    private void validateTarget(Station source, Station target) {
        if (source.equals(target)) {
            throw new InvalidSectionDirectionException("출발지와 도착지는 같을 수 없습니다");
        }
    }

    public boolean contains(Station station) {
        return source.equals(station) || target.equals(station);
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
                ", source=" + source +
                ", target=" + target +
                ", distance=" + distance +
                '}';
    }
}
