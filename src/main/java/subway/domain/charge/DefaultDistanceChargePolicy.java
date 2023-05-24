package subway.domain.charge;

import subway.domain.vo.Charge;
import subway.domain.vo.Distance;

public class DefaultDistanceChargePolicy implements DistanceChargePolicy {
    private static final Charge BASIC_CHARGE = new Charge(1250);
    private static final Charge ADDITIONAL_CHARGE_STEP = new Charge(100);
    private static final Distance BASIC_DISTANCE = new Distance(10);
    private static final Distance ADDITIONAL_DISTANCE = new Distance(50);
    private static final Distance ADDITIONAL_DISTANCE_STEP = new Distance(5);
    private static final Distance MAX_ADDITIONAL_DISTANCE_STEP = new Distance(8);

    @Override
    public Charge apply(Distance totalDistance) {
        if (totalDistance.isSmallOrEqualThan(BASIC_DISTANCE)) {
            return BASIC_CHARGE;
        }
        if (totalDistance.isSmallOrEqualThan(ADDITIONAL_DISTANCE)) {
            Distance additonalDistance = totalDistance.substract(BASIC_DISTANCE);
            Charge additionalCharge = ADDITIONAL_CHARGE_STEP.multiply(Math.ceil(additonalDistance.divide(ADDITIONAL_DISTANCE_STEP).getValue()));
            return BASIC_CHARGE.add(additionalCharge);
        }
        Distance additionalDistanceWithin50 = ADDITIONAL_DISTANCE.substract(BASIC_DISTANCE);
        Charge additionalChargeWithin50 = ADDITIONAL_CHARGE_STEP.multiply(additionalDistanceWithin50.divide(ADDITIONAL_DISTANCE_STEP).getValue());
        Distance additionalDistanceBeyond50 = totalDistance.substract(ADDITIONAL_DISTANCE);
        Charge additionalChargeBeyond50 = ADDITIONAL_CHARGE_STEP.multiply(Math.ceil(additionalDistanceBeyond50.divide(MAX_ADDITIONAL_DISTANCE_STEP).getValue()));
        return BASIC_CHARGE.add(additionalChargeWithin50).add(additionalChargeBeyond50);
    }
}
