package subway.domain.station;

import java.util.List;

public interface StationRepository {

    Station insert(Station station);

    Station findStationById(Long stationId);

    List<Station> findStationsByLineId(Long lineId);

    void remove(Station stationToDelete);
}
