package subway.line.domain;

import static subway.line.exception.line.LineExceptionType.NON_POSITIVE_DISTANCE;
import static subway.line.exception.line.LineExceptionType.UP_AND_DOWN_STATION_IS_SAME;

import java.util.Objects;
import subway.line.exception.line.LineException;

public class Section {

    private final Station up;
    private final Station down;
    private final int distance;

    public Section(final Station up, final Station down, final int distance) {
        validateSameStation(up, down);
        validateDistance(distance);
        this.up = up;
        this.down = down;
        this.distance = distance;
    }

    private void validateSameStation(final Station up, final Station down) {
        if (up.equals(down)) {
            throw new LineException(UP_AND_DOWN_STATION_IS_SAME);
        }
    }

    private void validateDistance(final int distance) {
        if (distance <= 0) {
            throw new LineException(NON_POSITIVE_DISTANCE);
        }
    }

    public Section plus(final Section section) {
        validateIsLinked(section);
        return new Section(this.up, section.down, distance + section.distance);
    }

    private void validateIsLinked(final Section section) {
        if (!isUpThan(section)) {
            throw new LineException("연속되지 않은 두 구간을 더할 수 없습니다.");
        }
    }

    public boolean isUpThan(final Section section) {
        return this.down.equals(section.up);
    }

    public boolean isDownThan(final Section section) {
        return this.up.equals(section.down);
    }

    public Section minus(final Section section) {
        final int interval = interval(section);
        if (!hasSameUpOrDownStation(section)) {
            throw new LineException("두 구간은 뺄 수 없는 관계입니다.");
        }
        return remainSection(section, interval);
    }

    private int interval(final Section section) {
        final int interval = this.distance - section.distance;
        validateDistance(interval);
        return interval;
    }

    public boolean hasSameUpOrDownStation(final Section section) {
        return this.up.equals(section.up) || this.down.equals(section.down);
    }

    private Section remainSection(final Section section, final int dist) {
        if (this.up.equals(section.up)) {
            return new Section(section.down, this.down, dist);
        }
        return new Section(this.up, section.up, dist);
    }

    public Section reverse() {
        return new Section(down, up, distance);
    }

    public boolean contain(final Station station) {
        return up.equals(station) || down.equals(station);
    }

    public boolean containsAllStation(final Section section) {
        return contain(section.up) && contain(section.down);
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
        return distance == section.distance
                && Objects.equals(up, section.up)
                && Objects.equals(down, section.down);
    }

    @Override
    public int hashCode() {
        return Objects.hash(up, down, distance);
    }

    public Station up() {
        return up;
    }

    public Station down() {
        return down;
    }

    public int distance() {
        return distance;
    }
}
