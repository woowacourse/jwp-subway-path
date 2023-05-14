package subway.repository.dao;

public class LineSectionStationJoinDto {

    private final Long lineId;
    private final String lineName;
    private final String lineColor;
    private final Long upStationId;
    private final String upStationName;
    private final Long downStationId;
    private final String downStationName;
    private final Long sectionId;
    private final Long sectionDistance;

    public LineSectionStationJoinDto(final Long lineId, final String lineName, final String lineColor,
        final Long upStationId, final String upStationName, final Long downStationId, final String downStationName,
        final Long sectionId, final Long sectionDistance) {
        this.lineId = lineId;
        this.lineName = lineName;
        this.lineColor = lineColor;
        this.upStationId = upStationId;
        this.upStationName = upStationName;
        this.downStationId = downStationId;
        this.downStationName = downStationName;
        this.sectionId = sectionId;
        this.sectionDistance = sectionDistance;
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

    public Long getStartStationId() {
        return upStationId;
    }

    public String getStartStationName() {
        return upStationName;
    }

    public Long getEndStationId() {
        return downStationId;
    }

    public String getEndStationName() {
        return downStationName;
    }

    public Long getSectionId() {
        return sectionId;
    }

    public Long getSectionDistance() {
        return sectionDistance;
    }
}
