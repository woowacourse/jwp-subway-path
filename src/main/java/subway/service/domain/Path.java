package subway.service.domain;

public class Path {

    private final Direction direction;
    private final LineProperty lineProperty;
    private final Station nextStation;
    private final Distance distance;

    public Path(Direction direction, LineProperty lineProperty, Station nextStation, Distance distance) {
        this.direction = direction;
        this.lineProperty = lineProperty;
        this.nextStation = nextStation;
        this.distance = distance;
    }

    public LineProperty getLineProperty() {
        return lineProperty;
    }

    public Direction getDirection() {
        return direction;
    }

    public Station getNextStation() {
        return nextStation;
    }

    public Integer getDistance() {
        return distance.getValue();
    }

    @Override
    public String toString() {
        return "Path{" +
                "direction=" + direction +
                ", lineProperty=" + lineProperty +
                ", nextStation=" + nextStation +
                ", distance=" + distance +
                '}';
    }

}