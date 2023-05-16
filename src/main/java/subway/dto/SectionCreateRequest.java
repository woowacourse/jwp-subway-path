package subway.dto;

public class SectionCreateRequest {

    private String baseStation;
    private String newStation;
    private String direction;
    private Integer distance;

    SectionCreateRequest() {
    }

    public SectionCreateRequest(final String baseStation, final String newStation, final String direction,
        final Integer distance) {
        this.baseStation = baseStation;
        this.newStation = newStation;
        this.direction = direction;
        this.distance = distance;
    }

    public String getBaseStation() {
        return baseStation;
    }

    public String getNewStation() {
        return newStation;
    }

    public String getDirection() {
        return direction;
    }

    public Integer getDistance() {
        return distance;
    }
}
