package subway.repository;

import subway.domain.section.Sections;
import subway.domain.station.Station;

public interface PathRepository {
    Sections findAllSections();

    Station findStationById(Long id);
}
