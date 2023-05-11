package subway.domain.repository;

import subway.domain.Station;

import java.util.List;

public interface StationRepository {
    void createStation(final Station station);

    List<Station> findAll();

    Station findById(Long stationIdRequest);

    void deleteById(Long stationIdRequest);
}
