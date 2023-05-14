package subway.persistence.entity;

public class SectionEntity {

    private final Long id;
    private final Long lineId;
    private final String left;
    private final String right;
    private final Integer distance;

    public SectionEntity(Long lineId, String left, String right, Integer distance) {
        this(null, lineId, left, right, distance);
    }

    public SectionEntity(Long id, Long lineId, String left, String right, Integer distance) {
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

    public String getLeft() {
        return left;
    }

    public String getRight() {
        return right;
    }

    public Integer getDistance() {
        return distance;
    }
}
