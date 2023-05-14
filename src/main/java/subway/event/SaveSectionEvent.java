package subway.event;

public class SaveSectionEvent {


    private final Long lineId;
    private final Long upStation;
    private final Long downStation;
    private final Boolean direction;

    public SaveSectionEvent(final Long lineId, final Long upStation, final Long downStation, final Boolean direction) {
        this.lineId = lineId;
        this.upStation = upStation;
        this.downStation = downStation;
        this.direction = direction;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getUpStation() {
        return upStation;
    }

    public Long getDownStation() {
        return downStation;
    }

    public Boolean getDirection() {
        return direction;
    }
}
