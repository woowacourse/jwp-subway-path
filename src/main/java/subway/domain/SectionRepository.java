package subway.domain;

import java.util.List;

public interface SectionRepository {

    void updateAllSectionsInLine(Line line, List<Section> lineSections);

    List<Section> readSectionsByLine(Line line);
}
