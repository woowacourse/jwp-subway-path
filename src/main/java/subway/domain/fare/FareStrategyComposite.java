package subway.domain.fare;

import java.util.List;
import subway.domain.subway.Passenger;
import subway.domain.subway.Subway;

public class FareStrategyComposite implements FareStrategy {

    private final List<FareStrategy> strategies;

    public FareStrategyComposite(final List<FareStrategy> strategies) {
        this.strategies = strategies;
    }

    @Override
    public double calculateFare(final double fare, final Passenger passenger, final Subway subway) {
        double baseFare = fare;
        for (final FareStrategy strategy : strategies) {
            baseFare = strategy.calculateFare(baseFare, passenger, subway);
        }
        return baseFare;
    }
}
