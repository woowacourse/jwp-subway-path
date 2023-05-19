package subway.domain.charge;

import subway.domain.Charge;

public interface AgeDiscountPolicy {
    Charge apply(int passengerAge, Charge charge);
}
