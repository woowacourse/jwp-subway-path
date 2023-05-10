package subway.domain;

public class StationEdge {

    private final Station up;
    private final Station down;
    private final int distance;

    public StationEdge(Station up, Station down, int distance) {
        this.up = up;
        this.down = down;
        this.distance = distance;
    }
}
