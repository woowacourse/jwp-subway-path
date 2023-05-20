package subway.domain.fare.policy;

import subway.domain.fare.FareInformation;

public interface DiscountPolicy {

    int calculate(final int fare, final FareInformation fareInformation);
}
