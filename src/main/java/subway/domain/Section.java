package subway.domain;

public class Section {

    private final Station up;
    private final Station down;
    private final int distance;

    public Section(final Station up, final Station down, final int distance) {
        validateDistance(distance, "역간 거리는 양수여야 합니다.");
        this.up = up;
        this.down = down;
        this.distance = distance;
    }

    private void validateDistance(final int distance, final String exceptionMessage) {
        if (distance <= 0) {
            throw new IllegalArgumentException(exceptionMessage);
        }
    }

    public Section plus(final Section section) {
        validateIsLinked(section);
        return new Section(this.up, section.down, distance + section.distance);
    }

    private void validateIsLinked(final Section section) {
        if (!isUpThan(section)) {
            throw new IllegalArgumentException("연속되지 않은 두 구간을 더할 수 없습니다.");
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
            throw new IllegalArgumentException("두 구간은 뺄 수 없는 관계입니다.");
        }
        return remainSection(section, interval);
    }

    private int interval(final Section section) {
        final int interval = this.distance - section.distance;
        validateDistance(interval, "현재 구간이 더 작아 차이를 구할 수 없습니다.");
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
