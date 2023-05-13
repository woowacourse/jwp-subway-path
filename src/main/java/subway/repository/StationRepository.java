package subway.repository;

import subway.domain.Station;

import java.util.List;

public interface StationRepository {

    Station findById(Long id);

    Station insert(Station station);

    void update(Long id, Station station);

    void deleteById(Long id);

    List<Station> findAll();
}
