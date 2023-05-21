package subway.infrastructure.entity;

public class SectionRow {

    private final Long id;
    private final Long lineId;
    private final Long upBound;
    private final Long downBound;
    private final Integer distance;

    public SectionRow(Long id, Long lineId, Long upBound, Long downBound, Integer distance) {
        this.id = id;
        this.lineId = lineId;
        this.upBound = upBound;
        this.downBound = downBound;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getUpBound() {
        return upBound;
    }

    public Long getDownBound() {
        return downBound;
    }

    public Integer getDistance() {
        return distance;
    }
}
