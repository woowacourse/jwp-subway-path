package subway.domain;

public class PathInfo {

    private final Distance distance;
    private final Direction direction;

    private PathInfo(final Distance distance, final Direction direction) {
        this.distance = distance;
        this.direction = direction;
    }

    public static PathInfo of(final Distance distance, final Direction direction) {
        return new PathInfo(distance, direction);
    }

    public boolean matchesByDirection(final Direction direction) {
        return this.direction.matches(direction);
    }

    public Distance getDistance() {
        return distance;
    }

    public Direction getDirection() {
        return direction;
    }
}
