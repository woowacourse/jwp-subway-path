package subway.domain;

public class FareCalculator {

    private static final int DEFAULT_FARE = 1250;
    private static final int DEFAULT_FARE_OVER_50KM = 2050;

    private FareCalculator() {
    }

    public static long calculate(int distance) {
        int result = DEFAULT_FARE;
        result = getDistanceFare(distance, result);
        return result;
    }

    public static long calculate(int distance, int surcharge) {
        return calculate(distance) + surcharge;
    }

    private static int getDistanceFare(int distance, int result) {
        if (distance >= 50) {
            result = DEFAULT_FARE_OVER_50KM;
            result += calculateFareOver50(distance);
        } else if (distance >= 10) {
            result += calculateFareOver10(distance);
        }
        return result;
    }

    private static long calculateFareOver10(int distance) {
        return (long) (((distance - 10) / 5) + 1) * 100;
    }

    private static long calculateFareOver50(int distance) {
        return (long) ((distance - 50) / 8) * 100;
    }
}
