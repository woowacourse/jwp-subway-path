package subway.domain;

import java.util.List;
import java.util.Objects;

public final class Path {
    private final Long id;
    private final Station up;
    private final Station down;
    private final int distance;

    public Path(final Long id, final Station up, final Station down, final int distance) {
        validate(distance);
        this.id = id;
        this.up = up;
        this.down = down;
        this.distance = distance;
    }

    public Path(final Station up, final Station down, final int distance) {
        this(null, up, down, distance);
    }

    private void validate(final int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("거리는 양의 정수여야 합니다.");
        }
    }

    public boolean isUpStationEquals(final Path another) {
        return up.equals(another.up);
    }

    public boolean isDownStationEquals(final Path another) {
        return down.equals(another.down);
    }

    public boolean contains(final Station station) {
        return up.equals(station) || down.equals(station);
    }

    public boolean isDownPath(final Path path) {
        if (this.equals(path)) {
            return false;
        }

        return down.equals(path.up);
    }

    public boolean isUpPath(final Path path) {
        if (this.equals(path)) {
            return false;
        }

        return up.equals(path.down);
    }

    public List<Path> divideBy(final Path middle) {
        if (distance <= middle.distance) {
            throw new IllegalArgumentException("기존의 거리보다 길 수 없습니다.");
        }
        final int newDistance = distance - middle.distance;

        if (up.equals(middle.up)) {
            final Path newPath = new Path(middle.down, down, newDistance);
            return List.of(middle, newPath);
        }

        final Path newPath = new Path(up, middle.up, newDistance);
        return List.of(middle, newPath);
    }

    public Path merge(final Path another) {
        if (down.equals(another.up)) {
            return new Path(up, another.down, distance + another.distance);
        }
        if (up.equals(another.down)) {
            return new Path(another.up, down, distance + another.distance);
        }

        throw new IllegalStateException("두 경로를 합칠 수 없습니다.");
    }

    public Long getId() {
        return id;
    }

    public Station getUp() {
        return up;
    }

    public Station getDown() {
        return down;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Path)) return false;
        final Path path = (Path) o;
        if (id != null) {
            return id.equals(path.id);
        }
        return Objects.equals(up, path.up) && Objects.equals(down, path.down);
    }

    @Override
    public int hashCode() {
        return Objects.hash(up, down, distance);
    }
}
