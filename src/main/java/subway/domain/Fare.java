package subway.domain;

public class Fare {

    private static final int BASIC_FARE = 1_250;
    private static final int BASIC_DISTANCE = 10;
    private static final int ADDITIONAL_FARE = 100;
    private static final int PER_5_KM = 5;
    private static final int PER_8_KM = 8;
    private static final int DISTANCE_CHARGED_PER_5_KM = 40;

    private final int fare;

    private Fare(final int fare) {
        this.fare = fare;
    }

    public static Fare from(final int distance){
        return new Fare(calculateFare(distance));
    }

    private static int calculateFare(final int distance) {
        if (distance <= BASIC_DISTANCE) {
            return BASIC_FARE;
        }
        return BASIC_FARE + calculateOverBasicFare(distance - BASIC_DISTANCE);
    }

    private static int calculateOverBasicFare(final int leftDistance) {
        if(leftDistance <= DISTANCE_CHARGED_PER_5_KM) {
            return calculateAdditionalFare(PER_5_KM, leftDistance);
        }
        return calculateAdditionalFare(PER_5_KM, DISTANCE_CHARGED_PER_5_KM) + calculateAdditionalFare(PER_8_KM, leftDistance - DISTANCE_CHARGED_PER_5_KM);
    }

    private static int calculateAdditionalFare(final int specified, final int leftDistance) {
        return (int) ((Math.ceil((leftDistance -1) / specified) + 1) * ADDITIONAL_FARE);
    }

    public int getFare() {
        return fare;
    }
}
