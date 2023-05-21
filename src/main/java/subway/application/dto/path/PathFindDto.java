package subway.application.dto.path;

public class PathFindDto {
    private long sourceStationId;
    private long destStationId;

    public PathFindDto(long sourceStationId, long destStationId) {
        this.sourceStationId = sourceStationId;
        this.destStationId = destStationId;
    }

    public long getSourceStationId() {
        return sourceStationId;
    }

    public long getDestStationId() {
        return destStationId;
    }
}
