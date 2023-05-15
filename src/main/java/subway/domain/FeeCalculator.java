package subway.domain;

public class FeeCalculator {

    private static final Integer DEFAULT_MONEY = 1250;

    public static long calculate(int distance) {
        int result = DEFAULT_MONEY;
        if (distance >= 10) {
            result += calculateOverFare(distance);
        }
        if (distance >= 50) {
            result += calculateOverFare2(distance);
        }
        return result;
    }

    private static long calculateOverFare(int distance) {
        return (long) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }

    private static long calculateOverFare2(int distance) {
        return (long) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }
}
