package subway.line.domain;

public enum FareCriteria {
  FIRST_SECTION(10, 5),
  SECOND_SECTION(50, 8);


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
