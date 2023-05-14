package subway.domain.line.dto;

public class LineWithSectionRes {

    private final Long lineId;
    private final String lineName;
    private final String lineColor;
    private final Long sourceStationId;
    private final String sourceStationName;
    private final Long targetStationId;
    private final String targetStationName;
    private final Integer distance;

    public LineWithSectionRes(final Long lineId, final String lineName, final String lineColor,
                              final Long sourceStationId, final String sourceStationName,
                              final Long targetStationId, final String targetStationName, final Integer distance) {
        this.lineId = lineId;
        this.lineName = lineName;
        this.lineColor = lineColor;
        this.sourceStationId = sourceStationId;
        this.sourceStationName = sourceStationName;
        this.targetStationId = targetStationId;
        this.targetStationName = targetStationName;
        this.distance = distance;
    }

    public boolean isSourceOrTargetStation(final String stationName) {
        return sourceStationName.equals(stationName) || targetStationName.equals(stationName);
    }

    public boolean isSourceOrTargetStation(final Long stationId) {
        return sourceStationId.equals(stationId) || targetStationId.equals(stationId);
    }

    public Long getStationIdByStationName(final String stationName) {
        if (sourceStationName.equals(stationName)) {
            return sourceStationId;
        }
        return targetStationId;
    }

    public String getStationNameByStationId(final Long stationId) {
        if (sourceStationId.equals(stationId)) {
            return sourceStationName;
        }
        return targetStationName;
    }

    public Long getLineId() {
        return lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public String getLineColor() {
        return lineColor;
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public String getSourceStationName() {
        return sourceStationName;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }

    public String getTargetStationName() {
        return targetStationName;
    }

    public Integer getDistance() {
        return distance;
    }
}
