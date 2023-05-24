package subway.application.fare;

import org.springframework.stereotype.Component;
import subway.domain.Fare;

import java.util.List;

@Component
public class DistanceRateFareCalculator implements FareCalculator {

    private static final Fare DEFAULT_FARE = Fare.from(1250);

    private final List<DistanceRateFare> distanceRateFares;

    public DistanceRateFareCalculator(List<DistanceRateFare> distanceRateFares) {
        this.distanceRateFares = distanceRateFares;
    }

    @Override
    public Fare calculateFare(FareCondition fareCondition) {
        Fare fare = DEFAULT_FARE;
        for (DistanceRateFare calculator : distanceRateFares) {
            fare = fare.plus(calculator.calculateFare(fareCondition.getDistance()).getFare());
        }

        return fare;
    }
}
