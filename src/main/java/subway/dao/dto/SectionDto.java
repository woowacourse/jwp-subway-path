package subway.dao.dto;

import subway.domain.Section;
import subway.domain.Station;

public class SectionDto {
    private final Long id;
    private final Long startStationId;
    private final Long endStationId;
    private final String startStationName;
    private final String endStationName;
    private final int distance;

    public SectionDto(Long id, Long startStationId, Long endStationId, String startStationName, String endStationName, int distance) {
        this.id = id;
        this.startStationId = startStationId;
        this.endStationId = endStationId;
        this.startStationName = startStationName;
        this.endStationName = endStationName;
        this.distance = distance;
    }

    public Section toDomain() {
        return new Section(id, new Station(startStationId, startStationName), new Station(endStationId, endStationName), distance);
    }
}
