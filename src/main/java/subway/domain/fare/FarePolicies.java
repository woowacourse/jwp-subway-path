package subway.domain.fare;

import java.util.List;
import org.springframework.stereotype.Component;
import subway.domain.subwaymap.LineStation;

@Component
public class FarePolicies implements FarePolicy {

    private final List<FarePolicy> policies = List.of(
        new DistanceFarePolicy(),
        new LineFarePolicy(),
        new AgeFarePolicy()
    );

    @Override
    public int calculate(final int fare, final List<LineStation> lineStations, final int distance, final int age) {
        validate(fare, distance, age);

        int currentFare = fare;
        for (FarePolicy policy : policies) {
            currentFare = policy.calculate(currentFare, lineStations, distance, age);
        }
        return currentFare;
    }
}
