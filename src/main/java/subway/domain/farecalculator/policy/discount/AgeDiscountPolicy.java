package subway.domain.farecalculator.policy.discount;

import java.util.List;

import org.springframework.stereotype.Component;

import subway.dto.FareResponse;

@Component
public class AgeDiscountPolicy implements DiscountPolicy{
    @Override
    public List<FareResponse> discount(Integer fare) {
        return AgeDiscountRule.computeFares(fare);
    }
}
