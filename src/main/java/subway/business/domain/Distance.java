package subway.business.domain;

public class Distance {
    private final int distance;
    private static final int MIN_LENGTH = 1;

    public Distance(int distance) {
        validate(distance);
        this.distance = distance;
    }

    public void validate(int distance) {
        if(distance < MIN_LENGTH) {
            throw new IllegalArgumentException("거리는 1보다 작을 수 없습니다.");
        }
    }

    public int getDistance() {
        return distance;
    }
}
