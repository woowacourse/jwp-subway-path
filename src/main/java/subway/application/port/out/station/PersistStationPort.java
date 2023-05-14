package subway.application.port.out.station;

import subway.domain.Station;

public interface PersistStationPort {

    long create(Station station);

    void update(Station station);

    void deleteById(long stationId);
}
