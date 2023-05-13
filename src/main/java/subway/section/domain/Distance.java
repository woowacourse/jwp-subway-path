package subway.section.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
@EqualsAndHashCode
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
}
