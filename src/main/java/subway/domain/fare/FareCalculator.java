package subway.domain.fare;

public class FareCalculator {
    private final FareStrategy fareStrategy;

    public FareCalculator(final FareStrategy fareStrategy) {
        this.fareStrategy = fareStrategy;
    }

    public Fare calculate(final int value) {
        return fareStrategy.calculate(value);
    }
}
