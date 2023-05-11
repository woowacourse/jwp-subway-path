package subway.domain;

public class Distance {
    private final int distance;

    public Distance(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("지하철 역 간 거리는 양의 정수만 가능합니다.");
        }
        this.distance = distance;
    }
}
