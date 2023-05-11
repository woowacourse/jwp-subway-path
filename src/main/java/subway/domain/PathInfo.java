package subway.domain;

public class PathInfo {

    private final Distance distance;
    private final RelationStatus status;

    private PathInfo(final Distance distance, final RelationStatus status) {
        this.distance = distance;
        this.status = status;
    }

    public static PathInfo of(final Distance distance, final RelationStatus status) {
        return new PathInfo(distance, status);
    }

    public PathInfo add(final Distance other, final RelationStatus status) {
        final Distance distance = this.distance.add(other);
        return new PathInfo(distance, status);
    }

    public PathInfo minus(final Distance other, final RelationStatus status) {
        final Distance distance = this.distance.minus(other);
        return new PathInfo(distance, status);
    }

    public boolean isGreaterThanOrEqualTo(final Distance target) {
        return this.distance.isGreaterThanOrEqualTo(target);
    }

    public boolean isUpStation() {
        return status.isUp();
    }

    public Distance getDistance() {
        return distance;
    }

}
