package subway.dto;

import subway.domain.entity.SectionEntity;
import subway.domain.vo.Distance;

public class SectionSaveRequest {

    private final Long id;
    private final Long lineId;
    private final Long upStationId;
    private final Long downStationId;
    private final Distance distance;

    private SectionSaveRequest(
            final Long id,
            final Long lineId,
            final Long upStationId,
            final Long downStationId,
            final Distance distance
    ) {
        this.id = id;
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static SectionSaveRequest of(SectionEntity sectionEntity) {
        return new SectionSaveRequest(
                null,
                sectionEntity.getLineId(),
                sectionEntity.getUpStationId(),
                sectionEntity.getDownStationId(),
                Distance.from(sectionEntity.getDistance())
        );
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance.getDistance();
    }

}
