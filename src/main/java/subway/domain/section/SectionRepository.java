package subway.domain.section;

import subway.domain.line.Line;

import java.util.List;

public interface SectionRepository {

    void updateAllSectionsInLine(Line line, List<Section> lineSections);

    List<Section> readSectionsByLine(Line line);

    List<Section> readAllSections();
}
