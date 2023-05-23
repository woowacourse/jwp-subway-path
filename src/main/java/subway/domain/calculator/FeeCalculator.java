package subway.domain.calculator;

public interface FeeCalculator {
    int calculate(final double distance);

    int calculate(final int distance);
}
