package subway.domain.fare;

import java.util.List;
import subway.domain.subwaymap.LineStation;

public interface FarePolicy {

    int calculate(int fare, final List<LineStation> lineStations, final int distance, final int age);

    default void validate(final int fare, final int distance, final int age) {
        if (fare < 0) {
            throw new IllegalArgumentException("요금은 0보다 작을 수 없습니다.");
        }
        if (distance < 0) {
            throw new IllegalArgumentException("거리는 0보다 작을 수 없습니다.");
        }
        if (age < 1) {
            throw new IllegalArgumentException("나이는 1보다 작을 수 없습니다.");
        }
    }
}
