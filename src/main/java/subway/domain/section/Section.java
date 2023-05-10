package subway.domain.section;

import subway.entity.StationEntity;

public class Section {

    private final Long id;
    private final Long lineId;
    private final StationEntity upStationEntity;
    private final StationEntity downStationEntity;
    private final int distance;

    public Section(final Long id, final Long lineId, final StationEntity upStationEntity, final StationEntity downStationEntity, final int distance) {
        this.id = id;
        this.lineId = lineId;
        this.upStationEntity = upStationEntity;
        this.downStationEntity = downStationEntity;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public StationEntity getUpStation() {
        return upStationEntity;
    }

    public StationEntity getDownStation() {
        return downStationEntity;
    }

    public int getDistance() {
        return distance;
    }
}
