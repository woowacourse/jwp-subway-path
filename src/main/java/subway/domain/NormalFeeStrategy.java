package subway.domain;

public class NormalFeeStrategy implements FeeStrategy {
    @Override
    public int calculate(int distance) {
        if (distance <= 10) {
            return 1250;
        }
        if (distance <= 50) {
            return 1250 + ((int)(Math.ceil((double)(distance - 10) / 5)) * 100);
        }
        return 1250 + (40 / 5) * 100 + ((int)(Math.ceil((double)(distance - 10) / 8)) * 100);
    }
}
