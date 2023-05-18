package subway.domain.section;

import java.util.List;

public interface SectionRepository {

    Section insert(Section sectionToAdd);

    List<Section> findSectionsByLineId(Long lineId);

    void remove(Section sectionToModify);
}
