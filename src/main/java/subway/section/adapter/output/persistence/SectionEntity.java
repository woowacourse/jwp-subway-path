package subway.section.adapter.output.persistence;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class SectionEntity {
    private final long id;
    private final String firstStation;
    private final String secondStation;
    private final int distance;
    private final long lineId;
    
    public SectionEntity(
            final long id,
            final String firstStation,
            final String secondStation,
            final int distance,
            final long lineId
    ) {
        this.id = id;
        this.firstStation = firstStation;
        this.secondStation = secondStation;
        this.distance = distance;
        this.lineId = lineId;
    }
}
