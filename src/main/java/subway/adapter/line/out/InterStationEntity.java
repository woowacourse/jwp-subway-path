package subway.adapter.line.out;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import subway.domain.interstation.InterStation;

@Getter
@RequiredArgsConstructor
public class InterStationEntity {

    private final Long id;
    private final Long lineId;
    private final long upStationId;
    private final long downStationId;
    private final long distance;

    public static InterStationEntity of(final InterStation interStation, final Long lineId) {
        return new InterStationEntity(interStation.getId(),
            lineId,
            interStation.getUpStationId(),
            interStation.getDownStationId(),
            interStation.getDistance().getValue());
    }

    public static InterStationEntity of(final InterStationEntity interStationEntity, final long lineId) {
        return new InterStationEntity(interStationEntity.getId(),
            lineId,
            interStationEntity.getUpStationId(),
            interStationEntity.getDownStationId(),
            interStationEntity.getDistance());
    }

    public InterStation toInterStation() {
        return new InterStation(id, upStationId, downStationId, distance);
    }
}
