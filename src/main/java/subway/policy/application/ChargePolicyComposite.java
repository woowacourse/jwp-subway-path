package subway.policy.application;

import java.util.List;
import subway.value_object.Money;
import subway.line.domain.Line;
import subway.policy.infrastructure.DiscountCondition;
import subway.policy.domain.SubwayDiscountPolicy;
import subway.policy.domain.SubwayFarePolicy;
import subway.line.domain.Station;

public class ChargePolicyComposite implements SubwayFarePolicy, SubwayDiscountPolicy {

  private final List<SubwayFarePolicy> farePolicies;
  private final List<SubwayDiscountPolicy> discountPolicies;

  public ChargePolicyComposite(
      final List<SubwayFarePolicy> farePolicies,
      final List<SubwayDiscountPolicy> discountPolicies
  ) {
    this.farePolicies = farePolicies;
    this.discountPolicies = discountPolicies;
  }

  @Override
  public Money discount(final DiscountCondition discountCondition, final Money price) {
    return discountPolicies.stream()
        .reduce(price, (money, subwayDiscountPolicy) ->
            subwayDiscountPolicy.discount(discountCondition, money), (money1, money2) -> money2);
  }

  @Override
  public Money calculate(final List<Line> lines, final Station departure, final Station arrival) {
    return farePolicies.stream()
        .map(it -> it.calculate(lines, departure, arrival))
        .reduce(Money.ZERO, Money::add);
  }
}
