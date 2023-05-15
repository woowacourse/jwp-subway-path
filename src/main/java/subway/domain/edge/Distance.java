package subway.domain.edge;

public class Distance {

    private final Integer distance;

    public Distance(final Integer distance) {
        validate(distance);
        this.distance = distance;
    }

    private void validate(final Integer distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException();
        }
    }
}
