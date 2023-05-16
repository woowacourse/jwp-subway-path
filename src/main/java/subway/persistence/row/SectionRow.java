package subway.persistence.row;

public class SectionRow {

    private final Long id;
    private final Long lineId;
    private final String left;
    private final String right;
    private final Integer distance;

    public SectionRow(Long id, Long lineId, String left, String right, Integer distance) {
        this.id = id;
        this.lineId = lineId;
        this.left = left;
        this.right = right;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public String getUpBound() {
        return left;
    }

    public String getDownBound() {
        return right;
    }

    public Integer getDistance() {
        return distance;
    }
}
