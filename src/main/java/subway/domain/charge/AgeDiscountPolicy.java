package subway.domain.charge;

import subway.domain.vo.Charge;

public interface AgeDiscountPolicy {
    Charge apply(int passengerAge, Charge charge);
}
