package subway.domain.fare;

import java.util.List;
import org.springframework.stereotype.Component;
import subway.domain.subway.Passenger;
import subway.domain.subway.Subway;

@Component
public class FareStrategyComposite implements FareStrategy {

    private final List<FareStrategy> strategies = List.of(
            new DistanceFareStrategy(),
            new RouteFareStrategy(),
            new AgeFareStrategy()
    );

    @Override
    public double calculateFare(final double fare, final Passenger passenger, final Subway subway) {
        double baseFare = fare;
        for (final FareStrategy strategy : strategies) {
            baseFare = strategy.calculateFare(baseFare, passenger, subway);
        }
        return baseFare;
    }
}
