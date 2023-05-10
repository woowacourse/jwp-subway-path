package subway.dto.station;

import subway.domain.entity.StationEntity;

import java.util.List;

public class LineMapResponse {

    private final List<StationEntity> stations;

    public LineMapResponse(final List<StationEntity> stations) {
        this.stations = stations;
    }

    public static LineMapResponse from(final List<StationEntity> stations) {
        return new LineMapResponse(stations);
    }

    public List<StationEntity> getStations() {
        return stations;
    }
}
