package subway.domain;

public class Station {

    private static Long sequence = 1L;
    private final Long id;
    private final String name;
    private final Distance distance;

    public Station(String name, Distance distance) {
        this.id = sequence++;
        this.name = name;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Distance getDistance() {
        return distance;
    }
}
