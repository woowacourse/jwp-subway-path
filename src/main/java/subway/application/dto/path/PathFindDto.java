package subway.application.dto.path;

public class PathFindDto {
    private Long sourceStationId;
    private Long destStationId;

    public PathFindDto(Long sourceStationId, Long destStationId) {
        this.sourceStationId = sourceStationId;
        this.destStationId = destStationId;
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getDestStationId() {
        return destStationId;
    }
}
