package subway.line.domain;

public enum FareCriteria {
  FIRST(0, 0),
  SECOND(10, 5),
  THIRD(50, 8);


  private final int distanceFrom;

  private final int distancePer;

  FareCriteria(int distanceFrom, int distancePer) {
    this.distanceFrom = distanceFrom;
    this.distancePer = distancePer;
  }

  public int getDistanceFrom() {
    return distanceFrom;
  }

  public int getDistancePer() {
    return distancePer;
  }
}
