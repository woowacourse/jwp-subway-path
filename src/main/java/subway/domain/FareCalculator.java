package subway.domain;

import org.springframework.stereotype.Component;

@Component
public class FareCalculator {
    private static final double MIN_DISTANCE = 0;
    private static final int DEFAULT_FARE = 1250;
    private static final int OVER_10_FARE = 100;
    private static final int OVER_50_FARE = 100;
    private static final int OVER_10_CHARGE_UNIT = 5;
    private static final int OVER_50_CHARGE_UNIT = 8;
    private static final int TEN = 10;
    private static final int FIFTY = 50;


    public int calculate(Path path) {
        double distance = path.getDistance();
        double over50Distance = calculateOver50Distance(distance);
        double over10Distance = calculateOver10Distance(distance, over50Distance);
        return DEFAULT_FARE + calculateFareOver10(over10Distance) + calculateFareOver50(over50Distance);
    }

    private double calculateOver50Distance(double originDistance) {
        double distance = originDistance - FIFTY;
        if (distance <= MIN_DISTANCE) {
            return MIN_DISTANCE;
        }
        return distance;
    }

    private double calculateOver10Distance(double originDistance, double over50Distance) {
        double distance = originDistance - over50Distance - (double) TEN;
        if (distance <= MIN_DISTANCE) {
            return MIN_DISTANCE;
        }
        return distance;
    }

    private int calculateFareOver10(double distance) {
        return (int) ((Math.ceil((distance) / OVER_10_CHARGE_UNIT)) * OVER_10_FARE);
    }

    private int calculateFareOver50(double distance) {
        return (int) ((Math.ceil((distance) / OVER_50_CHARGE_UNIT)) * OVER_50_FARE);
    }
}
