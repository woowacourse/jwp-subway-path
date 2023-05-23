package subway.domain.fare;

import subway.domain.Sections;

public class DistanceBasedFarePolicy implements FarePolicy {

    @Override
    public boolean supports(FarePolicyRelatedParameters parameters) {
        return true;
    }

    @Override
    public int calculate(int fare, FarePolicyRelatedParameters parameters) {
        Sections sections = parameters.getSections();
        int distance = sections.getTotalDistance();
        return calculateAdditionalCharge(fare, distance);
    }

    private int calculateAdditionalCharge(int fare, int distance) {
        fare += calculateAdditionalCharge(distance, 10, 50, 100, 5);
        fare += calculateAdditionalCharge(distance, 50, Integer.MAX_VALUE, 100, 8);
        return fare;
    }

    private int calculateAdditionalCharge(int distance, int lowerBound, int upperBound, int unitFare, int unitDistance) {
        if (distance > lowerBound) {
            int targetRange = getTargetRange(distance, lowerBound, upperBound);
            return (int)((Math.ceil((double)targetRange / unitDistance)) * unitFare);
        }
        return 0;
    }

    private static int getTargetRange(int distance, int lowerBound, int upperBound) {
        if (distance > upperBound) {
            return upperBound - lowerBound;
        }
        return distance - lowerBound;
    }
}
