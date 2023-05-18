package subway.domain.fare;

import subway.domain.Sections;

public class DistanceFarePolicy implements FarePolicy {

    @Override
    public int calculate(Sections sections) {
        int distance = sections.getTotalDistance();
        return calculateAdditionalFare(distance);
    }

    private int calculateAdditionalFare(int distance) {
        int totalFare = 0;
        totalFare += calculateAdditionalFare(distance, 10, 50, 100, 5);
        totalFare += calculateAdditionalFare(distance, 50, Integer.MAX_VALUE, 100, 8);
        return totalFare;
    }

    private int calculateAdditionalFare(int distance, int lowerBound, int upperBound, int unitFare, int unitDistance) {
        if (distance > lowerBound) {
            int targetRange = (distance > upperBound) ? (upperBound - lowerBound) : (distance - lowerBound);
            return (int)((Math.ceil((double)targetRange / unitDistance)) * unitFare);
        }
        return 0;
    }
}
