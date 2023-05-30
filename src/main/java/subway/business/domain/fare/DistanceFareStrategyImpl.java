package subway.business.domain.fare;

public class DistanceFareStrategyImpl implements DistanceFareStrategy {
    public static final int BASIC_FARE = 1250;
    public static final int FARE_PER_RISE_STANDARD = 100;

    @Override
    public int calculateFare(int distance) {
        int fare = BASIC_FARE;
        int fareOfOver10KmLessThan50Km = calculateFareOfOver10KmLessThan50Km(distance, fare);
        return calculateFareOfOver50Km(distance, fareOfOver10KmLessThan50Km);
    }

    private int calculateFareOfOver10KmLessThan50Km(int distance, int fare) {
        int distanceOver10KmLessThan50Km = getDistanceOver10KmLessThan50Km(distance);
        if (distanceOver10KmLessThan50Km <= 0) {
            return fare;
        }
        int riseStandardKm = 5;
        int additionalFare = (int) ((Math.ceil((distanceOver10KmLessThan50Km - 1) / riseStandardKm) + 1) * FARE_PER_RISE_STANDARD);
        return fare + additionalFare;
    }

    private int calculateFareOfOver50Km(int distance, int fare) {
        int distanceOver50Km = getDistanceOver50Km(distance);
        if (distanceOver50Km <= 0) {
            return fare;
        }
        int riseStandardKm = 8;
        int additionalFare = (int) ((Math.ceil((distanceOver50Km - 1) / riseStandardKm) + 1) * FARE_PER_RISE_STANDARD);
        return fare + additionalFare;
    }

    private int getDistanceOver10KmLessThan50Km(int distance) {
        int distanceForFare = distance;
        if (distance > 50) {
            distanceForFare = 50;
        }
        return distanceForFare - 10;
    }

    private int getDistanceOver50Km(int distance) {
        return distance - 50;
    }
}
