package subway.application.dto;

public class SectionDto {
    private final String startStation;
    private final String endStation;
    private final Integer distance;

    public SectionDto(String startStation, String endStation, Integer distance) {
        this.startStation = startStation;
        this.endStation = endStation;
        this.distance = distance;
    }

    public String getStartStation() {
        return startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public Integer getDistance() {
        return distance;
    }
}
