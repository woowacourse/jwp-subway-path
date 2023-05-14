package subway.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SectionEntity {
    private Long id;
    private Long lineId;
    private Long preStationId;
    private Long stationId;
    private Long distance;

    public SectionEntity(Long lineId, Long preStationId, Long stationId, Long distance) {
        this(null, lineId, preStationId, stationId, distance);
    }
}
