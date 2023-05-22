package subway.dao.dto;

import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Station;

public class SectionDto {

    private final long id;
    private final long startStationId;
    private final long endStationId;
    private final String startStationName;
    private final String endStationName;
    private final double distance;

    public SectionDto(long id, long startStationId, long endStationId, String startStationName,
        String endStationName, double distance) {
        this.id = id;
        this.startStationId = startStationId;
        this.endStationId = endStationId;
        this.startStationName = startStationName;
        this.endStationName = endStationName;
        this.distance = distance;
    }

    public Section toDomain() {
        return new Section(id, new Station(startStationId, startStationName),
            new Station(endStationId, endStationName),
            Distance.from(distance));
    }
}