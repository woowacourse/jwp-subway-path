package subway.entity;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import subway.domain.Distance;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class SectionEntity {
    private final long upStationId;
    private final long downStationId;
    private final int distance;
    private final long lineId;

    public static SectionEntity of(long upStationId, long downStationId, Distance distance, long lineId) {
        return new SectionEntity(upStationId, downStationId, distance.getValue(), lineId);
    }

    public static SectionEntity of(long upStationId, long downStationId, int distance, long lineId) {
        return new SectionEntity(upStationId, downStationId, distance, lineId);
    }

    public static SectionEntity of(StationEntity upStationEntity, StationEntity downStationEntity, Distance distance, long lineId) {
        return new SectionEntity(upStationEntity.getId(), downStationEntity.getId(), distance.getValue(), lineId);
    }
}
