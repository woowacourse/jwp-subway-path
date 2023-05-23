package subway.service.domain;

import java.util.Set;

public class Fare {

    private final Integer value;

    private Fare(Integer value) {
        this.value = value;
    }

    public static Fare from(Integer totalDistance) {
        return new Fare(calculate(totalDistance));
    }

    public static Fare of(ShortestPathInfo shortestPathInfo,
                          FarePolicies farePolicy,
                          Age age) {
        int calculate = calculate(shortestPathInfo.getTotalDistance())
                + getAdditionalFare(shortestPathInfo.getUsedLines(), farePolicy);
        return new Fare(acceptAgeDiscount(calculate, age));
    }

    private static Integer getAdditionalFare(Set<LineProperty> usedLines, FarePolicies farePolicy) {
        int maxAdditionalFareByLine = 0;

        for (LineProperty lineProperty : usedLines) {
            maxAdditionalFareByLine = Math.max(maxAdditionalFareByLine, farePolicy.getAdditionalFareByLineProperty(lineProperty));
        }

        return maxAdditionalFareByLine;
    }

    private static Integer calculate(Integer totalDistance) {
        return 1250 + chargeMoreForEveryFiveLargerThanTenAndSmallerThanFifty(totalDistance)
                + chargeMoreForEveryEightLargerThanFifty(totalDistance);
    }

    private static Integer acceptAgeDiscount(Integer fare, Age age) {
        return fare - (int) ((fare - age.getAmountDeducted()) * age.getDiscountRate());
    }

    private static int chargeMoreForEveryFiveLargerThanTenAndSmallerThanFifty(int totalDistance) {
        totalDistance = Math.min(40, totalDistance - 10);
        int chargeFare = (int) Math.ceil(totalDistance / 5d) * 100;
        return Math.max(chargeFare, 0);
    }

    private static Integer chargeMoreForEveryEightLargerThanFifty(int totalDistance) {
        totalDistance -= 50;
        int chargeFare = (int) Math.ceil(totalDistance / 8d) * 100;
        return Math.max(chargeFare, 0);
    }

    public Integer getValue() {
        return value;
    }

}
