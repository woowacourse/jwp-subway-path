package subway.domain.fare;

import java.util.List;
import subway.domain.subwaymap.LineStation;

public class LineFarePolicy implements FarePolicy {

    @Override
    public int calculate(int fare, final List<LineStation> lineStations, final int distance, final int age) {
        validate(fare, distance, age);

        final int maxAdditionalFare = lineStations.stream()
            .mapToInt(lineStation -> lineStation.getLine().getAdditionalFare())
            .max()
            .orElseThrow(() -> new IllegalArgumentException("라인이 존재하지 않습니다."));
        return fare + maxAdditionalFare;
    }
}
