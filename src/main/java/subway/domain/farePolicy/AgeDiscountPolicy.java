package subway.domain.farePolicy;

import org.springframework.stereotype.Component;
import subway.application.FarePolicy;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.general.Age;
import subway.domain.general.Money;
import subway.dto.PassengerDto;

import java.util.List;

@Component
public class AgeDiscountPolicy implements FarePolicy {

    public static final int BASIC_DEDUCTION = 350;
    public static final int CHILD_START = 6;
    public static final int CHILD_END = 13;
    public static final int TEENAGER_START = 13;
    public static final int TEENAGER_END = 19;
    public static final int CHILD_DISCOUNT_RATE = 50;
    public static final int TEENAGER_DISCOUNT_RATE = 20;

    @Override
    public Money applyPolicies(Money money, List<Sections> allSections, List<Section> path, PassengerDto passengerDto, FarePolicyChain policyChain) {
        Money fare = money;

        Age age = Age.of(passengerDto.getAge());
        if (age.isSameOrOver(CHILD_START) && age.isSmaller(CHILD_END)) {
            fare = fare.minus(Money.of(BASIC_DEDUCTION))
                    .discount(CHILD_DISCOUNT_RATE);
        }
        if (age.isSameOrOver(TEENAGER_START) && age.isSmaller(TEENAGER_END)) {
            fare = fare.minus(Money.of(BASIC_DEDUCTION))
                    .discount(TEENAGER_DISCOUNT_RATE);
        }
        return policyChain.applyPolicy(fare, allSections, path, passengerDto);
    }
}
