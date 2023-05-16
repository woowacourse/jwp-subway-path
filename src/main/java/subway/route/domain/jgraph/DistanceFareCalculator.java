package subway.route.domain.jgraph;

import subway.route.domain.FareCalculator;

public class DistanceFareCalculator implements FareCalculator {

    @Override
    public int calculate(int totalDistance) {
        if (totalDistance < 0) {
            throw new IllegalArgumentException("디버깅 거리는 음수가 될 수 없습니다. totalDistance: " + totalDistance);
        }
        if (totalDistance == 0) {
            return 0;
        }

        int fare = 1250;
        int count5 = Math.min((totalDistance > 10 ? (totalDistance - 10) / 5 : 0), 40 / 5);
        int count8 = totalDistance > 50 ? (totalDistance - 50) / 8 : 0;

        return fare + 100 * (count5 + count8);
    }
}
