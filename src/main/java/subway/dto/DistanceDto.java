package subway.dto;

public class DistanceDto {

    private Integer distance;

    private DistanceDto() {
    }

    public DistanceDto(final Integer distance) {
        this.distance = distance;
    }

    public Integer getDistance() {
        return distance;
    }

}
