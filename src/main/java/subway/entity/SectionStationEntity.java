package subway.entity;

import subway.domain.Section;
import subway.domain.Station;

public class SectionStationEntity {

    private final Long lineId;
    private final Long upStationId;
    private final String upStationName;
    private final Long downStationId;
    private final String downStationName;
    private final int distance;

    public SectionStationEntity(final Long lineId, final Long upStationId, final String upStationName,
                                final Long downStationId, final String downStationName, final int distance) {
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.upStationName = upStationName;
        this.downStationId = downStationId;
        this.downStationName = downStationName;
        this.distance = distance;
    }

    public Section toDomain() {
        return new Section(
                new Station(upStationId, upStationName),
                new Station(downStationId, downStationName),
                distance
        );
    }
}
