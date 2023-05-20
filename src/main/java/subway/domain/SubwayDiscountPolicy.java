package subway.domain;

public interface SubwayDiscountPolicy {

    Money discount(final DiscountCondition discountCondition, final Money price);
}
