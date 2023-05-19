package subway.application.dto;

public class PathFindDto {
    private String sourceStationName;
    private String destStationName;

    public PathFindDto(String sourceStationName, String destStationName) {
        this.sourceStationName = sourceStationName;
        this.destStationName = destStationName;
    }

    public String getSourceStationName() {
        return sourceStationName;
    }

    public String getDestStationName() {
        return destStationName;
    }
}
