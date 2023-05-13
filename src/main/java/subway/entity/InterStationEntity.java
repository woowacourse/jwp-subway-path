package subway.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import subway.domain.interstation.InterStation;

@Getter
@RequiredArgsConstructor
public class InterStationEntity {

    private final Long id;
    private final Long lineId;
    private final long frontStationId;
    private final long backStationId;
    private final long distance;

    public static InterStationEntity of(final InterStation interStation, final Long lineId) {
        return new InterStationEntity(interStation.getId(),
                lineId,
                interStation.getFirstStation().getId(),
                interStation.getSecondStation().getId(),
                interStation.getDistance());
    }
}
