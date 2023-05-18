package subway.line.domain;

import java.util.Objects;


public class LineNameKey {

  private final Long leftStationId;
  private final Long rightStationId;

  public LineNameKey(Long leftStationId, Long rightStationId) {
    this.leftStationId = leftStationId;
    this.rightStationId = rightStationId;
  }

  public Long getLeftStationId() {
    return leftStationId;
  }

  public Long getRightStationId() {
    return rightStationId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LineNameKey that = (LineNameKey) o;
    return Objects.equals(leftStationId, that.leftStationId) && Objects.equals(rightStationId,
        that.rightStationId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(leftStationId, rightStationId);
  }
}
