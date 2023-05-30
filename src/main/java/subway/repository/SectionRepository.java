package subway.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;

@Transactional
@Repository
public interface SectionRepository {
    Sections findSectionsByLineId(final long lineId);

    Station addSection(final Section section, final long lineId);

    void addSections(final Sections addedSections, final long lineId);

    Station addStation(final Station station);

    Station findStationByName(final Station station);

    Station findStationById(final long stationId);

    void deleteSections(final Sections deletedSections, long lineId);

    boolean existStationByName(Station station);
}
