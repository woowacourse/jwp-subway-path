package subway.domain.fare;

public class FareCalculator {

    private static final int BASE_FARE = 1_250;

    public int calculate(double distance) {
        return BASE_FARE + calculateOverFare((int) distance);
    }

    private int calculateOverFare(int distance) {
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }
}
