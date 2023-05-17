package subway.domain.farecalculator.policy.discount;

import java.util.List;

import subway.dto.FareResponse;

public interface DiscountPolicy {
    List<FareResponse> discount(Integer fare);
}
