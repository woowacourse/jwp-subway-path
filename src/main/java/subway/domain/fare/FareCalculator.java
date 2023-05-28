package subway.domain.fare;

import org.springframework.stereotype.Component;

@Component
public class FareCalculator {

    private final FareStrategy fareStrategy;

    public FareCalculator(final FareStrategy fareStrategy) {
        this.fareStrategy = fareStrategy;
    }

    public int calculateFare(final int distance) {
        return fareStrategy.calculateFare(distance);
    }
}
