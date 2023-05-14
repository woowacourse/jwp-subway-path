package subway.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SectionEntity {
    private Long id;
    private Long lineId;
    private Long sourceStationId;
    private Long targetStationId;
    private Long distance;

    public SectionEntity(Long lineId, Long sourceStationId, Long targetStationId, Long distance) {
        this(null, lineId, sourceStationId, targetStationId, distance);
    }
}
