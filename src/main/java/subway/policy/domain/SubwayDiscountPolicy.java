package subway.policy.domain;

import subway.policy.infrastructure.DiscountCondition;
import subway.value_object.Money;

public interface SubwayDiscountPolicy {

  Money discount(final DiscountCondition discountCondition, final Money price);
}
