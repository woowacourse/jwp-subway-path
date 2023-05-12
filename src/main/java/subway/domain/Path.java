package subway.domain;

import java.util.List;
import java.util.Objects;

public final class Path {
    private final Station up;
    private final Station down;
    private final int distance;

    public Path(final Station up, final Station down, final int distance) {
        validate(distance);
        this.up = up;
        this.down = down;
        this.distance = distance;
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
        return Objects.equals(up, path.up) && Objects.equals(down, path.down);
    }

    @Override
    public int hashCode() {
        return Objects.hash(up, down, distance);
    }
}
