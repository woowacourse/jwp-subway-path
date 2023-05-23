package subway.domain.policy.discount;

import subway.domain.Money;

public interface SubwayDiscountPolicy {

  Money discount(final DiscountCondition discountCondition, final Money price);
}
