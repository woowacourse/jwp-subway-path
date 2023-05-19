package subway.service.domain;

public class Path {

    private final Direction direction;
    private final Station nextStation;
    private final Distance distance;

    public Path(Direction direction, Station nextStation, Distance distance) {
        this.direction = direction;
        this.nextStation = nextStation;
        this.distance = distance;
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
                ", nextStation=" + nextStation +
                ", distance=" + distance +
                '}';
    }

}