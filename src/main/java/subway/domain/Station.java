package subway.domain;

public class Station {

    private final String name;
    private final Distance distance;

    public Station(String name, Distance distance) {
        this.name = name;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public Distance getDistance() {
        return distance;
    }
}
