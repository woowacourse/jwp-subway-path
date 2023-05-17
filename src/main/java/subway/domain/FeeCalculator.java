package subway.domain;

public class FeeCalculator {
    private final FeeStrategy feeStrategy;

    public FeeCalculator(FeeStrategy feeStrategy) {
        this.feeStrategy = feeStrategy;
    }

    public int calculate(int distance) {
        return feeStrategy.calculate(distance);
    }
}
