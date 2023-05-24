package subway.application.fare;

import org.springframework.stereotype.Component;
import subway.domain.Distance;
import subway.domain.Fare;

@Component
public class DistanceOver50 extends DistanceRateFare {

    public DistanceOver50() {
        super(8, 100);
    }

    @Override
    public Fare calculateFare(Distance distance) {
        if (support(distance)) {
            return calculate(distance.minus(DISTANCE_50));
        }

        return Fare.zero();
    }

    @Override
    public boolean support(Distance distance) {
        return DISTANCE_50.isShorterThan(distance);
    }
}
