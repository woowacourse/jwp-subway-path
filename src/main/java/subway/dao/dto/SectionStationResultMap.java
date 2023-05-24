package subway.dao.dto;

public class SectionStationResultMap {

    private final Long sectionId;
    private final Integer distance;
    private final Long upStationId;
    private final String upStationName;
    private final Long downStationId;
    private final String downStationName;

    private final Long lineId;

    public SectionStationResultMap(Long sectionId, Integer distance, Long upStationId, String upStationName, Long downStationId, String downStationName, Long lineId) {
        this.sectionId = sectionId;
        this.distance = distance;
        this.upStationId = upStationId;
        this.upStationName = upStationName;
        this.downStationId = downStationId;
        this.downStationName = downStationName;
        this.lineId = lineId;
    }

    public Long getSectionId() {
        return sectionId;
    }

    public Integer getDistance() {
        return distance;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public String getUpStationName() {
        return upStationName;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public String getDownStationName() {
        return downStationName;
    }

    public Long getLineId() {
        return lineId;
    }
}
