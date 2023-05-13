package subway.section.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
@EqualsAndHashCode
public class Distance {
    private final Long distance;
    
    public Distance(final Long distance) {
        validateNull(distance);
        this.distance = distance;
    }
    
    private void validateNull(final Long distance) {
        if (Objects.isNull(distance)) {
            throw new IllegalArgumentException("거리는 null일 수 없습니다.");
        }
    }
}
