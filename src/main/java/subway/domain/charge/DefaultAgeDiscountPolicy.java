package subway.domain.charge;

import subway.domain.vo.Charge;

public class DefaultAgeDiscountPolicy implements AgeDiscountPolicy {

    private static final int CHILD_STANDARD_MINIMUM = 6;
    private static final int CHILD_STANDARD_MAXIMUM = 12;
    private static final int TEENAGER_STANDARD_MINIMUM = 13;
    private static final int TEENAGER_STANDARD_MAXIMUM = 18;
    private static final double CHILD_DEDUCTION_RATE = 0.5;
    private static final double TEENAGER_DEDUCTION_RATE = 0.2;
    private static final Charge BASIC_DEDUCTION_AMOUNT = new Charge(350);

    @Override
    public Charge apply(int passengerAge, Charge charge) {
        if (CHILD_STANDARD_MINIMUM <= passengerAge && passengerAge <= CHILD_STANDARD_MAXIMUM) {
            Charge basicDeduction = charge.substract(BASIC_DEDUCTION_AMOUNT);
            return basicDeduction.multiply(1-CHILD_DEDUCTION_RATE);
        }
        if (TEENAGER_STANDARD_MINIMUM <= passengerAge && passengerAge <= TEENAGER_STANDARD_MAXIMUM) {
            Charge basicDeduction = charge.substract(BASIC_DEDUCTION_AMOUNT);
            return basicDeduction.multiply(1 - TEENAGER_DEDUCTION_RATE);
        }
        return charge;
    }
}
