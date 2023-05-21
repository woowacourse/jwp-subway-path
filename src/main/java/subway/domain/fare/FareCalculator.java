package subway.domain.fare;

import org.springframework.stereotype.Component;

@Component
public class FareCalculator {

    private final FarePolicy farePolicy;

    public FareCalculator(FarePolicy farePolicy) {
        this.farePolicy = farePolicy;
    }

    public int calculate(int distance) {
        return farePolicy.calculateFare(distance);
    }
}
