package subway.application.port.out.station;

import subway.domain.Station;

public interface StationCommandPort {
    Long createStation(final Station station);
}
