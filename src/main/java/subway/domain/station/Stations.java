package subway.domain.station;

import java.util.List;
import subway.domain.station.Station;

public class Stations {

    private static final int INVALID_STATION_COUNT = 1;

    private final List<Station> stations;

    public Stations(final List<Station> stations) {
        validateStationCount(stations);
        this.stations = stations;
    }

    private void validateStationCount(final List<Station> stations) {
        if (stations.size() == INVALID_STATION_COUNT) {
            throw new IllegalArgumentException("노선의 역은 1개만 존재할 수 없습니다.");
        }
    }
}
