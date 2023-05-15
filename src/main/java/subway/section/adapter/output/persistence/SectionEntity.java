package subway.section.adapter.output.persistence;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class SectionEntity {
    private final Long id;
    private final Long firstStationId;
    private final Long secondStationId;
    private final Long distance;
    private final Long lineId;
    
    public SectionEntity(final Long firstStationId, final Long secondStationId, final Long distance, final Long lineId) {
        this(null, firstStationId, secondStationId, distance, lineId);
    }
}
