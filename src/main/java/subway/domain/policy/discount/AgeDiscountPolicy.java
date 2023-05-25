package subway.domain.policy.discount;

import java.util.EnumMap;
import java.util.Map;
import subway.domain.Money;

public class AgeDiscountPolicy implements SubwayDiscountPolicy {

  private static final Map<AgeGroup, DiscountValue> policyMap = new EnumMap<>(AgeGroup.class);

  static {
    policyMap.put(AgeGroup.CHILD, new DiscountValue(350, 50));
    policyMap.put(AgeGroup.TEENAGER, new DiscountValue(350, 20));
    policyMap.put(AgeGroup.NONE, new DiscountValue(0, 0));
  }

  @Override
  public Money discount(final DiscountCondition discountCondition, final Money price) {

    final AgeGroup ageGroup = AgeGroup.findAgeGroup(discountCondition.getAge());
    final DiscountValue discountValue = policyMap.get(ageGroup);

    return price.minus(discountValue.getDiscountPrice())
        .calculateDiscountedPrice(discountValue.getPercent());
  }

  private static class DiscountValue {

    private final int discountPrice;
    private final int percent;

    public DiscountValue(final int discountPrice, final int percent) {
      this.discountPrice = discountPrice;
      this.percent = percent;
    }

    public int getDiscountPrice() {
      return discountPrice;
    }

    public int getPercent() {
      return percent;
    }
  }
}
