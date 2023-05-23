package subway.domain.policy.discount;

public class DiscountCondition {

  private final Integer age;

  public DiscountCondition(final Integer age) {
    this.age = age;
  }

  public Integer getAge() {
    return age;
  }
}
