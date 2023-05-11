package subway.domain.section;

public class Distance {

    private final int distance;

    // TODO: 정적 팩터리 메서드로 validatePositive, validatePositiveOrZero 나누기
    public Distance(final int distance) {
        validate(distance);
        this.distance = distance;
    }

    private void validate(final int distance) {
        if (distance < 0) {
            throw new IllegalArgumentException("거리는 음의 정수가 될 수 없습니다.");
        }
    }

    public int getDistance() {
        return distance;
    }
}
