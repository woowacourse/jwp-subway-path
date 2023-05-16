package subway.service.section.domain;

import java.util.List;

public class DistanceFeePolicy implements FeePolicy {

    private static int BASE_FEE = 1250;
    private static int BASE_FEE_BOUNDARY = 10;
    private static int SURCHARGE = 100;
    private static int SURCHARGE_BOUNDARY = 50;
    private static int FIRST_SURCHARGE_UNIT = 5;
    private static int SECOND_SURCHARGE_UNIT = 8;

    @Override
    public int calculateFee(List<SectionEdge> edges) {
        double totalDistance = edges.stream()
                .mapToDouble(SectionEdge::getWeight)
                .sum();

        if (totalDistance <= BASE_FEE_BOUNDARY) {
            return BASE_FEE;
        }
        if (totalDistance <= SURCHARGE_BOUNDARY) {
            return BASE_FEE + addFirstSurcharge(totalDistance);
        }

        int firstMaxSurcharge = (SURCHARGE_BOUNDARY - BASE_FEE_BOUNDARY) / FIRST_SURCHARGE_UNIT * SURCHARGE;
        return BASE_FEE + firstMaxSurcharge + addSecondSurcharge(totalDistance);

    }

    private int addFirstSurcharge(double totalDistance) {
        double surchargeDistance = totalDistance - BASE_FEE_BOUNDARY;
        int fee = ((int) (surchargeDistance / FIRST_SURCHARGE_UNIT) * SURCHARGE);

        if (surchargeDistance % FIRST_SURCHARGE_UNIT != 0) {
            return fee + SURCHARGE;
        }
        return fee;
    }

    private int addSecondSurcharge(double totalDistance) {
        double surchargeDistance = totalDistance - SURCHARGE_BOUNDARY;
        int fee = ((int) surchargeDistance / SECOND_SURCHARGE_UNIT * SURCHARGE);

        if (surchargeDistance % SECOND_SURCHARGE_UNIT != 0) {
            return fee + SURCHARGE;
        }
        return fee;
    }
}
