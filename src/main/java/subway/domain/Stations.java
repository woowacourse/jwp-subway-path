package subway.domain;

import java.util.List;
import subway.exception.DuplicateException;
import subway.exception.ErrorCode;

public class Stations {
    private final List<Station> stations;

    public Stations(final List<Station> stations) {
        this.stations = stations;
    }


    public boolean isEmpty() {
        return stations.isEmpty();
    }

    public boolean contains(final Station station) {
        return stations.contains(station);
    }

    public void validateStations(final Station startStation, final Station endStation) {
        if (contains(startStation) && contains(endStation)) {
            throw new DuplicateException(ErrorCode.DUPLICATE_STATION);
        }
    }
}
