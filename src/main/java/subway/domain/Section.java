package subway.domain;

import java.util.Objects;

public class Section {
    private static final int MIN_DISTANCE = 1;

    private final Station source;
    private final Station target;
    private final int distance;

    public Section(final String source, final String target, final int distance) {
        this(new Station(source), new Station(target), distance);
    }

    public Section(final Station source, final Station target, final int distance) {
        validate(source, target, distance);
        this.source = source;
        this.target = target;
        this.distance = distance;
    }

    private void validate(final Station source, final Station target, final int distance) {
        if (distance < MIN_DISTANCE) {
            throw new IllegalArgumentException(String.format("거리 정보는 %d 이상이어야 합니다.", MIN_DISTANCE));
        }
        if (source.equals(target)) {
            throw new IllegalArgumentException("출발역과 도착역은 동일할 수 없습니다.");
        }
    }

    public boolean contains(final Station station) {
        return isSource(station) || isTarget(station);
    }

    public boolean isSource(final Station station) {
        return source.equals(station);
    }

    public boolean isTarget(final Station station) {
        return target.equals(station);
    }

    public boolean isLongOrEqualThan(final int distance) {
        return this.distance <= distance;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Section)) {
            return false;
        }
        final Section section = (Section) o;
        return distance == section.distance && Objects.equals(source, section.source) && Objects.equals(
                target, section.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target, distance);
    }

}
