package subway.domain.path;

public class Fare {

    public static int calculateFare(final double distance) {
        int fare = 1250;
        if (10 < distance && distance <= 50) {
            final double additionalWeight = (distance - 10) / 5;
            fare += Math.ceil(additionalWeight) * 100;
        }
        if (50 < distance) {
            final double additionalWeight = (distance - 50) / 8;
            fare += 800 + Math.ceil(additionalWeight) * 100;
        }
        return fare;
    }
}
