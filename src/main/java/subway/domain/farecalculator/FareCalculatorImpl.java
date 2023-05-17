package subway.domain.farecalculator;

import java.util.List;

import org.springframework.stereotype.Component;

import subway.domain.farecalculator.policy.additional.AdditionalFarePolicy;
import subway.domain.farecalculator.policy.discount.DiscountPolicy;
import subway.domain.farecalculator.policy.distance.FareByDistancePolicy;
import subway.dto.FareResponse;
import subway.dto.SectionResponse;

@Component
public class FareCalculatorImpl implements FareCalculator{
    private final FareByDistancePolicy fareByDistancePolicy;
    private final AdditionalFarePolicy additionalFarePolicy;
    private final DiscountPolicy discountPolicy;

    public FareCalculatorImpl(
            FareByDistancePolicy fareByDistancePolicy,
            AdditionalFarePolicy additionalFarePolicy,
            DiscountPolicy discountPolicy
    ) {
        this.fareByDistancePolicy = fareByDistancePolicy;
        this.additionalFarePolicy = additionalFarePolicy;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public List<FareResponse> calculate(List<SectionResponse> sections, Integer distance) {
        final int fareByDistance = fareByDistancePolicy.calculateFare(distance);
        final int additionalFare = additionalFarePolicy.calculateAdditionalFare(sections);
        return discountPolicy.discount(fareByDistance + additionalFare);
    }
}
