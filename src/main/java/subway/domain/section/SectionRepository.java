package subway.domain.section;

import subway.Entity.LineEntity;

import java.util.List;

public interface SectionRepository {

    void updateAllSectionsInLine(LineEntity lineEntity, List<Section> lineSections);

    List<Section> readSectionsByLine(LineEntity lineEntity);

    List<Section> readAllSections();
}
