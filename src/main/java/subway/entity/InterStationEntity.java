package subway.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InterStationEntity {

    private final Long id;
    private final Long lineId;
    private final Long startStationId;
    private final Long endStationId;
    private final long distance;
}
