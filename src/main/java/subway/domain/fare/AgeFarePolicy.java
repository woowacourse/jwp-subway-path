package subway.domain.fare;

import java.util.List;
import subway.domain.subwaymap.LineStation;

public class AgeFarePolicy implements FarePolicy {

    @Override
    public int calculate(int fare, final List<LineStation> lineStations, final int distance, final int age) {
        validate(fare, distance, age);

        final int discountAmount = PassengerAge.get(age).getDiscountAmount(fare);
        return fare - discountAmount;
    }
}
