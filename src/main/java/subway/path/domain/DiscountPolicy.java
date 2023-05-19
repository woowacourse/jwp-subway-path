package subway.path.domain;

import subway.payment.domain.discount.DiscountResult;

public interface DiscountPolicy {

    DiscountResult discount(final int fee);
}
