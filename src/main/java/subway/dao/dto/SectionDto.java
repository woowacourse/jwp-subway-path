package subway.dao.dto;

import subway.domain.Section;
import subway.domain.Station;

public class SectionDto {
    private final String startStationName;
    private final String endStationName;
    private final int distance;

    public SectionDto(String startStationName, String endStationName, int distance) {
        this.startStationName = startStationName;
        this.endStationName = endStationName;
        this.distance = distance;
    }

    public Section toDomain() {
        return new Section(new Station(startStationName), new Station(endStationName), distance);
    }

    public String getStartStationName() {
        return startStationName;
    }

    public String getEndStationName() {
        return endStationName;
    }

    public int getDistance() {
        return distance;
    }
}
