package subway.station.application.port.output;

import subway.station.domain.Station;

public interface GetStationByIdPort {
    Station getStationById(Long id);
}
