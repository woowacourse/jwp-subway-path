package subway.domain.fare;

import java.util.List;
import org.springframework.stereotype.Component;
import subway.domain.section.Distance;

@Component
public class DistanceFareStrategies {

    private final List<FareStrategy> strategies;

    public DistanceFareStrategies(final List<FareStrategy> strategies) {
        this.strategies = strategies;
    }

    public Fare getTotalFare(final Distance distance) {
        return strategies.stream()
                .filter(strategy -> strategy.isSatisfied(distance))
                .map(strategy -> strategy.calculate(distance))
                .reduce(this::maxFare).stream()
                .findFirst()
                .orElseThrow();
    }

    private Fare maxFare(final Fare firstFare, final Fare secondFare) {
        if (firstFare.isMoreThan(secondFare)) {
            return firstFare;
        }

        return secondFare;
    }
}
