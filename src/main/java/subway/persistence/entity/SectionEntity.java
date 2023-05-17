package subway.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SectionEntity {
    private final Long id;
    private final Long lineId;
    private final Long preStationId;
    private final Long stationId;
    private final Long distance;

    public SectionEntity(Long lineId, Long preStationId, Long stationId, Long distance) {
        this(null, lineId, preStationId, stationId, distance);
    }
}
