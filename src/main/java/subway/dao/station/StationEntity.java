package subway.dao.station;

import subway.domain.station.Station;
import subway.domain.station.StationName;

public class StationEntity {
    private final Long stationId;
    private final String name;

    public StationEntity(final String name) {
        this(null, name);
    }

    public StationEntity(final Long stationId, final String name) {
        this.stationId = stationId;
        this.name = name;
    }

    public Station toStation() {
        return new Station(
                this.stationId,
                new StationName(this.name)
        );
    }

    public Long getStationId() {
        return stationId;
    }

    public String getName() {
        return name;
    }

}
