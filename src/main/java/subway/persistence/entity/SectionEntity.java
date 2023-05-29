package subway.persistence.entity;

public class SectionEntity {
    private Long id;
    private String upStation;
    private String downStation;
    private Long lineId;
    private Long distance;

    public SectionEntity(final Long id, final String upStation, final String downStation, final Long lineId, final Long distance) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.lineId = lineId;
        this.distance = distance;
    }

    public SectionEntity(final String upStation, final String downStation, final Long lineId, final Long distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.lineId = lineId;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public String getUpStation() {
        return upStation;
    }

    public String getDownStation() {
        return downStation;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getDistance() {
        return distance;
    }
}
