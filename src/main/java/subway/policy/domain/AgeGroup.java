package subway.policy.domain;

import java.util.Arrays;
import java.util.Objects;

public enum AgeGroup {

  TEENAGER(13, 19),
  CHILD(6, 13),
  NONE(0, 0);

  private final int ageLowLimit;
  private final int ageHighLimit;

  AgeGroup(final int ageLowLimit, final int ageHighLimit) {
    this.ageLowLimit = ageLowLimit;
    this.ageHighLimit = ageHighLimit;
  }

  public static AgeGroup findAgeGroup(final Integer target) {
    return Arrays.stream(values())
        .filter(age -> Objects.nonNull(target))
        .filter(age -> target >= age.ageLowLimit && target < age.ageHighLimit)
        .findAny()
        .orElse(NONE);
  }
}
