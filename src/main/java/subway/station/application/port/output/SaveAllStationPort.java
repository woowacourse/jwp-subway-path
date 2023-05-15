package subway.station.application.port.output;

import subway.station.domain.Station;

import java.util.List;

public interface SaveAllStationPort {
    void saveAll(final List<Station> stations);
}
