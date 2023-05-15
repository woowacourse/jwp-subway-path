package subway.domain.vo;

public class Distance {

    private final int distance;

    private Distance(final int distance) {
        this.distance = distance;
    }

    public static Distance from(final int distance) {
        return new Distance(distance);
    }

    public int getDistance() {
        return distance;
    }

}
