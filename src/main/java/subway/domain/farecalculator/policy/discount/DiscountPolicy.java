package subway.domain.farecalculator.policy.discount;

import subway.dto.FareResponse;

public interface DiscountPolicy {
    FareResponse discount(Integer age, Integer fare);
}
