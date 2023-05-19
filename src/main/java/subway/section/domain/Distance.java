package subway.section.domain;

import java.util.Objects;

public class Distance {
    private static final int MIN_DISTANCE = 1;
    
    private final Long distance;
    
    public Distance(final Long distance) {
        validateDistance(distance);
        this.distance = distance;
    }
    
    private void validateDistance(final Long distance) {
        validateNull(distance);
        validateOutOfRange(distance);
    }
    
    private void validateNull(final Long distance) {
        if (Objects.isNull(distance)) {
            throw new IllegalArgumentException("거리는 null일 수 없습니다.");
        }
    }
    
    private void validateOutOfRange(final Long distance) {
        if (distance < MIN_DISTANCE) {
            throw new IllegalArgumentException("최소 거리는 1입니다.");
        }
    }
    
    public Long subtract(final long otherDistance) {
        return this.distance - otherDistance;
    }
    
    public Long add(final Distance otherDistance) {
        return this.distance + otherDistance.distance;
    }
    
    public boolean lessThanOrEqualTo(final long additionalDistance) {
        return this.distance <= additionalDistance;
    }
    
    public Long getDistance() {
        return distance;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Distance distance1 = (Distance) o;
        return Objects.equals(distance, distance1.distance);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }
    
    @Override
    public String toString() {
        return "Distance{" +
                "distance=" + distance +
                '}';
    }
}
