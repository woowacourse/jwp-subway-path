package subway.dto;

import subway.domain.section.Distance;

public class DistanceResponse {

    private Integer distance;

    public DistanceResponse() {
    }

    public DistanceResponse(final Integer distance) {
        this.distance = distance;
    }

    public static DistanceResponse of(final Distance distance) {
        return new DistanceResponse(distance.getDistance());
    }

    public Integer getDistance() {
        return distance;
    }
}
