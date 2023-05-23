package subway.domain.fare.discount;

import subway.domain.PassengerType;
import subway.domain.fare.FareInfo;
import subway.domain.fare.FarePolicy;
import subway.domain.vo.Money;

public class PassengerDiscountPolicy implements FarePolicy {

    @Override
    public FareInfo doCalculate(final FareInfo fareInfo) {
        final PassengerType passengerType = fareInfo.getPassengerType();
        final PassengerDiscountType discountType = PassengerDiscountType.findBy(passengerType);
        final Money discount = discountType.doDiscount(fareInfo.getTotalFare());

        return fareInfo.minusFare(discount);
    }
}
