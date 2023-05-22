package subway.application.fare;

import org.springframework.stereotype.Component;
import subway.domain.Distance;
import subway.domain.Fare;

@Component
public class DistanceOver10Under50 extends DistanceRateFare {

    public DistanceOver10Under50() {
        super(5, 100);
    }

    @Override
    public Fare calculateFare(Distance distance) {
        if (support(distance)) {
            return getFare(distance);
        }

        return Fare.zero();
    }

    private Fare getFare(Distance distance) {
        if (DISTANCE_50.isShorterThan(distance)) {
            return calculate(DISTANCE_50.minus(DEFAULT_DISTANCE));
        }

        return calculate(distance.minus(DEFAULT_DISTANCE));
    }

    @Override
    public boolean support(Distance distance) {
        return DEFAULT_DISTANCE.isShorterThan(distance);
    }
}
