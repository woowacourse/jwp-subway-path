package subway.domain;

public class Section {

    private static final String NEGATIVE_DISTANCE_ERROR_MESSAGE = "거리는 양의 정수만 가능합니다.";
    private static final int DISTANCE_LOWER_BOUND = 0;

    private final Station upStation;
    private final Station downStation;
    private final int distance;

    public Section(final Station upStation, final Station downStation, final int distance) {
        validateDistance(distance);
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    private void validateDistance(final int distance) {
        if (distance <= DISTANCE_LOWER_BOUND) {
            throw new IllegalArgumentException(NEGATIVE_DISTANCE_ERROR_MESSAGE);
        }
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public int getDistance() {
        return distance;
    }
}
