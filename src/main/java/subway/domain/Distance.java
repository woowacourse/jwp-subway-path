package subway.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class Distance implements Comparable<Distance> {
    private static final String UNIT = "km";

    private double distance;
    private String unit;

    public Distance(double distance) {
        this.distance = distance;
        this.unit = UNIT;
    }

    public Distance add(Distance other) {
        return new Distance(distance + other.distance);
    }

    public Distance subtract(Distance other) {
        return new Distance(distance - other.distance);
    }

    @Override
    public int compareTo(Distance other) {
        if (distance < other.distance) {
            return -1;
        }
        if (distance > other.distance) {
            return 1;
        }
        return 0;
    }
}
