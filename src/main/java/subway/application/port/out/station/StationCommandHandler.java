package subway.application.port.out.station;

import subway.domain.Station;

public interface StationCommandHandler {
    Long createStation(final Station station);
}
