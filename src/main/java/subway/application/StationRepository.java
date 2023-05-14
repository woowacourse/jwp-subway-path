package subway.application;

import subway.domain.Station;

import java.util.List;

public interface StationRepository {

    Station insert(Station station);

    List<Station> findAll();

    Station findById(Long id);

    void update(Station newStation);

    void deleteById(Long id);
}
