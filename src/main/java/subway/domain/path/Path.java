package subway.domain.path;

import subway.domain.Station;

import java.util.ArrayList;
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

    public boolean isUpStation(final Station up) {
        return up.equals(this.up);
    }

    public boolean isDownStation(final Station down) {
        return down.equals(this.down);
    }

    public boolean contains(final Station station) {
        return up.equals(station) || down.equals(station);
    }

    public boolean isContinuous(final Path next) {
        if (equals(next)) {
            return false;
        }

        return down.equals(next.up);
    }

    public boolean isOverlapped(final Path other) {
        return isUpStation(other.up) || isDownStation(other.down);
    }

    public Path merge(final Path other) {
        if (isContinuous(other)) {
            return new Path(up, other.down, distance + other.distance);
        }
        if (other.isContinuous(this)) {
            return new Path(other.up, down, distance + other.distance);
        }

        throw new IllegalStateException("두 경로를 합칠 수 없습니다.");
    }

    public List<Path> divide(final Path divisor) {
        if (distance <= divisor.distance) {
            throw new IllegalArgumentException("기존의 거리보다 길 수 없습니다.");
        }
        if (!isOverlapped(divisor)) {
            throw new IllegalArgumentException("두 경로가 겹치지 않습니다.");
        }

        if (up.equals(divisor.up)) {
            return divideByUpPath(divisor);
        }
        return divideByDownPath(divisor);
    }

    private List<Path> divideByUpPath(final Path up) {
        final Path down = new Path(up.down, this.down, distance - up.distance);

        return List.of(up, down);
    }

    private List<Path> divideByDownPath(final Path down) {
        final Path up = new Path(this.up, down.up, distance - down.distance);

        return List.of(up, down);
    }

    public List<Station> getStations() {
        return new ArrayList<>(List.of(up, down));
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
