package subway.domain;

import subway.exception.InvalidInputException;

public class Distance {
    private final int distance;

    public Distance(int distance) {
        this.distance = distance;
    }

    public Distance subtract(Distance other) {
        if (this.distance <= other.distance) {
            throw new InvalidInputException("거리가 기존 구역 거리를 초과했습니다.");
        }
        return new Distance(this.distance - other.distance);
    }

    public Distance add(Distance other){
        return new Distance(this.distance + other.distance);
    }

    public int getDistance() {
        return distance;
    }
}
