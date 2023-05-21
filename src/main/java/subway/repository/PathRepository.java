package subway.repository;

import subway.domain.Sections;
import subway.domain.Station;

public interface PathRepository {
    Sections findAllSections();

    Station findStationById(Long id);
}
