package subway.domain.station.domain.repository;

import subway.domain.station.domain.Station;

import java.util.List;
import java.util.Map;

public interface StationRepository {

    Long insert(Station station);

    Station findById(Long id);

    Station findFinalUpStation(Long lineId);

    Station findFinalDownStation(Long lineId);

    Map<String, Station> getFinalStations(final Long lineId);

    List<Station> findAll();

    void updateById(Long id, Station station);

    void deleteById(Long id);

}
