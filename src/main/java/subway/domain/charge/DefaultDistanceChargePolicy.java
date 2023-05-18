package subway.domain.charge;

public class DefaultDistanceChargePolicy implements DistanceChargePolicy {
    private static final Charge BASIC_CHARGE = new Charge(1250);
    private static final Charge ADDITIONAL_CHARGE_STEP = new Charge(100);
    private static final int BASIC_DISTANCE = 10;
    private static final int ADDITIONAL_DISTANCE = 50;
    private static final int ADDITIONAL_DISTANCE_STEP = 5;
    private static final int MAX_ADDITIONAL_DISTANCE_STEP = 8;

    @Override
    public Charge apply(double totalDistance) {
        if (totalDistance <= BASIC_DISTANCE) {
            return BASIC_CHARGE;
        }
        if (totalDistance <= ADDITIONAL_DISTANCE) {
            double additonalDistance = totalDistance - BASIC_DISTANCE;
            Charge additionalCharge = ADDITIONAL_CHARGE_STEP.multiply(Math.ceil(additonalDistance / ADDITIONAL_DISTANCE_STEP));
            return BASIC_CHARGE.add(additionalCharge);
        }
        double additionalDistanceWithin50 = ADDITIONAL_DISTANCE - BASIC_DISTANCE;
        Charge additionalChargeWithin50 = ADDITIONAL_CHARGE_STEP.multiply(additionalDistanceWithin50 / ADDITIONAL_DISTANCE_STEP);
        double additionalDistanceBeyond50 = totalDistance - ADDITIONAL_DISTANCE;
        Charge additionalChargeBeyond50 = ADDITIONAL_CHARGE_STEP.multiply(Math.ceil(additionalDistanceBeyond50 / MAX_ADDITIONAL_DISTANCE_STEP));
        return BASIC_CHARGE.add(additionalChargeWithin50).add(additionalChargeBeyond50);
    }
}
