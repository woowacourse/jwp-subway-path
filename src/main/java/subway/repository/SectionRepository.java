package subway.repository;

import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;

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
