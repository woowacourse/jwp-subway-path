package subway.domain.farecalculator;

import java.util.List;

import org.springframework.stereotype.Component;

import subway.domain.farecalculator.policy.additional.AdditionalFarePolicy;
import subway.domain.farecalculator.policy.discount.DiscountPolicy;
import subway.domain.farecalculator.policy.distance.FareByDistancePolicy;
import subway.dto.FareResponse;
import subway.dto.SectionResponse;

@Component
public class DefaultFareCalculator implements FareCalculator{
    private final FareByDistancePolicy fareByDistancePolicy;
    private final AdditionalFarePolicy additionalFarePolicy;
    private final DiscountPolicy discountPolicy;

    public DefaultFareCalculator(
            FareByDistancePolicy fareByDistancePolicy,
            AdditionalFarePolicy additionalFarePolicy,
            DiscountPolicy discountPolicy
    ) {
        this.fareByDistancePolicy = fareByDistancePolicy;
        this.additionalFarePolicy = additionalFarePolicy;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public FareResponse calculate(List<SectionResponse> sections, Integer distance, Integer age) {
        final int fareByDistance = fareByDistancePolicy.calculateFare(distance);
        final int additionalFare = additionalFarePolicy.calculateAdditionalFare(sections);
        return discountPolicy.discount(age,fareByDistance + additionalFare);
    }
}
