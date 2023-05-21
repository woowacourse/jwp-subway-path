package subway.domain.fare;

import org.springframework.stereotype.Component;

@Component
public class DistanceFarePolicy implements FarePolicy {

    private static final int BASIC_FARE = 1250;

    @Override
    public int calculateFare(int distance) {
        if (distance > 50) {
            return calculateFare(50) +
                    (int) Math.ceil((double) (distance - 50) / 8) * 100;
        }
        if (distance > 10) {
            return BASIC_FARE +
                    (int) Math.ceil((double) (distance - 10) / 5) * 100;
        }
        return BASIC_FARE;
    }
}
