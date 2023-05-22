package subway.dao.dto;

public class LineWithSection {

    private final Long id;
    private final Long lineId;
    private final String lineName;
    private final String lineColor;
    private final Integer lineExtraFare;
    private final Long sourceStationId;
    private final String sourceStationName;
    private final Long targetStationId;
    private final String targetStationName;
    private final Integer distance;

    public LineWithSection(final Long id, final Long lineId, final String lineName, final String lineColor,
                           final Integer lineExtraFare, final Long sourceStationId, final String sourceStationName,
                           final Long targetStationId, final String targetStationName, final Integer distance) {
        this.id = id;
        this.lineId = lineId;
        this.lineName = lineName;
        this.lineColor = lineColor;
        this.lineExtraFare = lineExtraFare;
        this.sourceStationId = sourceStationId;
        this.sourceStationName = sourceStationName;
        this.targetStationId = targetStationId;
        this.targetStationName = targetStationName;
        this.distance = distance;
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

    public Integer getLineExtraFare() {
        return lineExtraFare;
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
