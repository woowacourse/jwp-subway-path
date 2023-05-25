package subway.domain.policy;

import java.util.List;
import subway.domain.Money;
import subway.domain.line.Line;
import subway.domain.policy.discount.DiscountCondition;
import subway.domain.policy.discount.SubwayDiscountPolicy;
import subway.domain.policy.fare.SubwayFarePolicy;
import subway.domain.station.Station;

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
