package subway.domain.farecalculator;

import org.springframework.stereotype.Component;

@Component
public class BasicFareCalculator implements FareCalculator {

    public static final Integer BASIC_FARE = 1250;
    public static final Integer ADDITIONAL_FARE = 100;
    public static final Integer MINIMUM_DISTANCE = 10;
    public static final Integer MAXIMUM_BASIC_FARE_DISTANCE = 50;
    public static final double BASIC_ADDITIONAL_CHARGED_DISTANCE = 5;
    public static final double DISCOUNT_ADDITIONAL_CHARGED_DISTANCE = 8;

    @Override
    public Integer calculateFare(Integer distance) {
        if (distance <= MINIMUM_DISTANCE) {
            return BASIC_FARE;
        }

        if (distance > MAXIMUM_BASIC_FARE_DISTANCE) {
            final int discountDistance = distance - MAXIMUM_BASIC_FARE_DISTANCE;
            final int basicFare = calculateBasicFare(MAXIMUM_BASIC_FARE_DISTANCE);
            final int discountedAdditionalFare =
                    (int) Math.ceil(discountDistance / DISCOUNT_ADDITIONAL_CHARGED_DISTANCE) * ADDITIONAL_FARE;
            return basicFare + discountedAdditionalFare;
        }

        return calculateBasicFare(distance);
    }

    private static int calculateBasicFare(Integer distance) {
        final double factor = Math.ceil((distance - MINIMUM_DISTANCE) / BASIC_ADDITIONAL_CHARGED_DISTANCE);
        return (int) factor * ADDITIONAL_FARE + BASIC_FARE;
    }
}
