package subway.domain;

import java.util.Objects;

public class Distance {

  public static final Distance DEFAULT_DISTANCE = new Distance(10);
  public static final Distance LONG_DISTANCE = new Distance(50);
  public static final Distance MID_DISTANCE = new Distance(40);

  private final int value;

  public Distance(final int value) {
    validateDistance(value);
    this.value = value;
  }

  private void validateDistance(final int distance) {
    if (distance <= 0) {
      throw new IllegalArgumentException("거리는 0이 될 수 없습니다.");
    }
  }

  public boolean isShorterThan(final Distance other) {
    return value <= other.value;
  }

  public Distance minus(final Distance other) {
    return new Distance(value - other.value);
  }

  public Distance plus(final Distance other) {
    return new Distance(value + other.value);
  }

  public Distance calculateDistanceUnit(final int rate) {
    return new Distance(((value - 1) / rate) + 1);
  }

  public boolean isDefaultDistance() {
    return value <= DEFAULT_DISTANCE.value;
  }

  public boolean isLongDistance() {
    return value > LONG_DISTANCE.value;
  }

  public int getValue() {
    return value;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final Distance distance = (Distance) o;
    return value == distance.value;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
