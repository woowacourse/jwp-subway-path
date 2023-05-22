package subway.application.fare;

import org.springframework.stereotype.Component;
import subway.domain.Distance;
import subway.domain.Fare;

@Component
public class DistanceUnder10 extends DistanceRateFare {

    public DistanceUnder10() {
        super(0, 0);
    }

    @Override
    public Fare calculateFare(Distance distance) {
        return Fare.zero();
    }

    boolean support(Distance distance) {
        return distance.isShorterThanAndEqual(distance);
    }
}
