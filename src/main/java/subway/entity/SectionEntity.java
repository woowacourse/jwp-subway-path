package subway.entity;

public class SectionEntity {

    private final Long lineId;
    private final Long upperStation;
    private final Long lowerStation;
    private final int distance;

    public SectionEntity(Long lineId, Long upperStation, Long lowerStation, int distance) {
        this.lineId = lineId;
        this.upperStation = upperStation;
        this.lowerStation = lowerStation;
        this.distance = distance;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getUpperStation() {
        return upperStation;
    }

    public Long getLowerStation() {
        return lowerStation;
    }

    public int getDistance() {
        return distance;
    }
}
