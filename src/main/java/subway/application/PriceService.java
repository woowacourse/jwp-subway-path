package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.Distance;
import subway.domain.Price;

@Service
public class PriceService {

    private static final Price BASIC_FARE = Price.from(1250);
    private static final Price ADDITION_FARE = Price.from(100);
    private static final Price NO_ADDITION_FARE = Price.from(0);
    private static final Distance FIVE_KM_DISTANCE = Distance.from(5);
    private static final Distance EIGHT_KM_DISTANCE = Distance.from(8);
    private static final Distance TEN_KM_DISTANCE = Distance.from(10);
    private static final Distance FIFTY_KM_DISTANCE = Distance.from(50);
    private static final Distance MAXIMUM_DISTANCE = Distance.from(1_000_000);

    public Price getSubwayFare(final Distance distance) {
        final Price fareForTenToFiftyKm = calculateAdditionFare(distance, TEN_KM_DISTANCE, FIFTY_KM_DISTANCE, FIVE_KM_DISTANCE);
        final Price fareForOverFiftyKm = calculateAdditionFare(distance, FIFTY_KM_DISTANCE, MAXIMUM_DISTANCE, EIGHT_KM_DISTANCE);

        return BASIC_FARE.plus(fareForTenToFiftyKm).plus(fareForOverFiftyKm);
    }

    private Price calculateAdditionFare(final Distance distance,
                                        final Distance minimumDistance,
                                        final Distance maximumDistance,
                                        final Distance distancePerFare) {
        if (distance.isLessThanOrEqualTo(minimumDistance)) {
            return NO_ADDITION_FARE;
        }

        final Distance remainDistance = getDistanceForIntervalCalculating(distance, minimumDistance, maximumDistance);

        return Price.from(calculateSurcharge(remainDistance, distancePerFare));
    }

    private Distance getDistanceForIntervalCalculating(final Distance distance,
                                                       final Distance minimumDistance,
                                                       final Distance maximumDistance) {
        final int originValue = distance.getDistance();
        final int minimumValue = minimumDistance.getDistance();
        final int maximumValue = maximumDistance.getDistance();

        if (originValue > maximumValue) {
            return Distance.from(maximumValue - minimumValue);
        }

        return Distance.from(originValue - minimumValue);
    }

    private int calculateSurcharge(final Distance distance, final Distance farePerDistance) {
        final int originValue = distance.getDistance();
        final int perFareValue = farePerDistance.getDistance();
        final int additionFare = ADDITION_FARE.getPrice();

        return (((originValue - 1) / perFareValue) + 1) * additionFare;
    }
}
