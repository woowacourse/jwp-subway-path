package subway.station.application.port.output;

import subway.station.domain.Station;

public interface SaveStationPort {
    Long saveStation(Station station);
}
