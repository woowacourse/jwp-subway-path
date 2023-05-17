package subway.domain.section;

public class SectionInfo {

    private final Distance distance;
    private final Direction direction;

    private SectionInfo(final Distance distance, final Direction direction) {
        this.distance = distance;
        this.direction = direction;
    }

    public static SectionInfo of(final Distance distance, final Direction direction) {
        return new SectionInfo(distance, direction);
    }

    public Distance calculateMiddleDistance(final Distance other) {
        if (other.isGreaterThanOrEqualTo(distance)) {
            throw new IllegalArgumentException("등록되는 구간 중간에 다른 역이 존재합니다.");
        }

        return distance.minus(other);
    }

    public boolean matchesByDirection(final Direction direction) {
        return this.direction.matches(direction);
    }

    public int distance() {
        return distance.getDistance();
    }

    public Distance getDistance() {
        return distance;
    }

    public Direction direction() {
        return direction;
    }
}
