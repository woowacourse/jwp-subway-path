package subway.application.fare;

import java.util.List;
import org.springframework.stereotype.Component;
import subway.domain.fare.DistanceProportionRange;
import subway.domain.fare.Fare;

@Component
public class DistanceFareCalculator {
    private static final int SURCHARGE_FARE = 100;

    private final List<DistanceProportionRange> distanceProportionRanges;

    public DistanceFareCalculator() {
        this.distanceProportionRanges = List.of(
                DistanceProportionRange.FIRST_DISTANCE_RANGE,
                DistanceProportionRange.SECOND_DISTANCE_RANGE
        );
    }

    public Fare calculateFareByDistance(final int distance) {
        Fare totalFare = new Fare(0);
        for (final DistanceProportionRange distanceProportionRange : distanceProportionRanges) {
            final Fare additionalFare = calculate(distance, distanceProportionRange);
            totalFare = totalFare.add(additionalFare);
        }

        return totalFare;
    }

    public Fare calculate(final int distance, final DistanceProportionRange distanceProportionRange) {
        if (distance <= distanceProportionRange.getLowerBoundRange()) {
            return new Fare(0);
        }

        if (distance > distanceProportionRange.getUpperBoundRange()) {
            return calculateExtraFare(distanceProportionRange.getUpperBoundRange(), distanceProportionRange);
        }

        return calculateExtraFare(distance, distanceProportionRange);
    }

    private Fare calculateExtraFare(final int distance, final DistanceProportionRange distanceProportionRange) {
        final int extraDistance = distance - distanceProportionRange.getLowerBoundRange();
        final int surchargeDistanceUnit = distanceProportionRange.getSurchargeDistanceUnit();
        final int extraFare = (int) (Math.ceil((extraDistance - 1) / surchargeDistanceUnit) + 1) * SURCHARGE_FARE;

        return new Fare(extraFare);
    }
}
