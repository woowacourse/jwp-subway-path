package subway.domain.farecalculator.policy.discount;

import org.springframework.stereotype.Component;

import subway.dto.FareResponse;

@Component
public class AgeDiscountPolicy implements DiscountPolicy{
    @Override
    public FareResponse discount(Integer age, Integer fare) {
        return AgeDiscountRule.computeFare(age, fare);
    }
}
