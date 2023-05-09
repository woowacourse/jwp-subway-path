package subway.domain;

public class MyEdge {

    private final MyStation from;
    private final MyStation to;
    private final int distance;

    public MyEdge(MyStation from, MyStation to, int distance) {
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    public MyStation getFrom() {
        return from;
    }

    public MyStation getTo() {
        return to;
    }

    public int getDistance() {
        return distance;
    }
}
