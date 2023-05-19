package subway.domain;

public class Distance {

    private static final Distance DISTANCE_FIVE_UNIT = new Distance(5);
    private static final Distance DISTANCE_EIGHT_UNIT = new Distance(8);
    private static final Distance FIRST_CHARGE_BOUND = new Distance(10);
    private static final Distance SECOND_CHARGE_BOUND = new Distance(50);
    private static final Charge EXTRA_CHARGE_UNIT = new Charge(100);
    private static final int BASIC_CHARGE = 1250;
    private final int value;

    public Distance(int value) {
        this.value = value;
    }

    public Charge calculateCharge(Charge extraCharge) {
        Charge charge = new Charge(BASIC_CHARGE).add(extraCharge);
        if (isLessAndEqualsThan(FIRST_CHARGE_BOUND)) {
            return charge;
        }
        if (isLessAndEqualsThan(SECOND_CHARGE_BOUND)) {
            final Charge overTenExtraCharge = calculateOverCharge(
                substract(FIRST_CHARGE_BOUND), DISTANCE_FIVE_UNIT);
            return charge.add(overTenExtraCharge);
        }
        final Charge tenToFiftyExtraCharge = calculateOverCharge(
            SECOND_CHARGE_BOUND.substract(FIRST_CHARGE_BOUND), DISTANCE_FIVE_UNIT);
        final Charge overFiftyExtraCharge = calculateOverCharge(
            substract(SECOND_CHARGE_BOUND), DISTANCE_EIGHT_UNIT);
        return charge.add(tenToFiftyExtraCharge).add(overFiftyExtraCharge);
    }

    private boolean isLessAndEqualsThan(Distance distance) {
        return value <= distance.value;
    }

    private Charge calculateOverCharge(Distance distance, Distance unit) {
        return new Charge(distance.substractOne().divide(unit) + 1).multiply(EXTRA_CHARGE_UNIT);
    }

    private Distance substractOne() {
        return new Distance(value - 1);
    }

    private int divide(Distance distance) {
        return value / distance.value;
    }

    private Distance substract(Distance distance) {
        return new Distance(value - distance.value);
    }

    public int getValue() {
        return value;
    }
}
