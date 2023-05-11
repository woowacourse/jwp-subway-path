package subway.repository;

import subway.domain.Station;

public interface StationRepository {

    Station findById(Long id);
}
