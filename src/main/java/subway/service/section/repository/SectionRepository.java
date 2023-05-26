package subway.service.section.repository;

import subway.service.line.domain.Line;
import subway.service.section.domain.Section;
import subway.service.section.domain.Sections;
import subway.service.station.domain.Station;

import java.util.List;
import java.util.Map;

public interface SectionRepository {
    Section insertSection(Section section, Line line);

    Sections findSectionsByLine(Line line);

    Map<Line, Sections> findSectionsByStation(Station station);

    void deleteSection(Section section);

    boolean isLastSectionInLine(Line line);

    List<Section> findAll();

    Map<Line, Sections> findAllSectionsPerLine();
}
