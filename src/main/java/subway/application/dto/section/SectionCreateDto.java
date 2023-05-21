package subway.application.dto.section;

public class SectionCreateDto {
    private Long lineId;
    private String startStationName;
    private String endStationName;
    private double distance;

    public SectionCreateDto(Long lineId, String startStationName, String endStationName, double distance) {
        this.lineId = lineId;
        this.startStationName = startStationName;
        this.endStationName = endStationName;
        this.distance = distance;
    }

    public Long getLineId() {
        return lineId;
    }

    public String getStartStationName() {
        return startStationName;
    }

    public String getEndStationName() {
        return endStationName;
    }

    public double getDistance() {
        return distance;
    }
}
