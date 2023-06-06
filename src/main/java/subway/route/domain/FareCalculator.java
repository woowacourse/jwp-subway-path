package subway.route.domain;

import subway.route.domain.distance.DistancePolicy;

public class FareCalculator {

    public long calculateFare(int distance) {
        DistancePolicy distancePolicy = DistancePolicy.from(distance);
        return distancePolicy.calculateFare(distance);
    }
}
 
