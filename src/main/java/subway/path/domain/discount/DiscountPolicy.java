package subway.path.domain.discount;

public interface DiscountPolicy {

    DiscountResult discount(final int fee);
}
