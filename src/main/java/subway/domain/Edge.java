package subway.domain;

public class Edge {

    private final int distance;

    public Edge(int distance) {
        validate(distance);
        this.distance = distance;
    }

    private void validate(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("역 간의 거리는 양수여야 합니다.");
        }
    }
}
