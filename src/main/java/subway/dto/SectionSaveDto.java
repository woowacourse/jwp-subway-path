package subway.dto;

public class SectionSaveDto {
    private String startStation;
    private String endStation;
    private Integer distance;

    private SectionSaveDto() {
    }

    public SectionSaveDto(String startStation, String endStation, Integer distance) {
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
