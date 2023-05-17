package subway.domain.path;

import subway.domain.Station;

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
