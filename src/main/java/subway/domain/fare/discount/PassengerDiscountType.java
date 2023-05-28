package subway.domain.fare.discount;

import subway.domain.PassengerType;
import subway.domain.vo.Money;
import subway.domain.vo.Percent;

import java.util.Arrays;

import static subway.domain.PassengerType.*;
import static subway.domain.vo.Percent.*;

public enum PassengerDiscountType {

    BABY_DISCOUNT(BABY, PERCENT_100),
    CHILDREN_DISCOUNT(CHILDREN, PERCENT_50),
    TEENAGER_DISCOUNT(TEENAGER, PERCENT_20),
    ADULT_DISCOUNT(ADULT, PERCENT_0);

    private final PassengerType passengerType;
    private final Percent percent;

    PassengerDiscountType(final PassengerType passengerType, final Percent percent) {
        this.passengerType = passengerType;
        this.percent = percent;
    }

    public static PassengerDiscountType findBy(final PassengerType passengerType) {
        return Arrays.stream(PassengerDiscountType.values())
                .filter(discountType -> discountType.passengerType == passengerType)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 승객 타입입니다."));
    }

    public Money doDiscount(final Money totalMoney) {
        return totalMoney.multiply(percent.getPercent());
    }
}
