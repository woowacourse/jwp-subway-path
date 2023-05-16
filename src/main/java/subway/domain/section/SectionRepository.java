package subway.domain.section;

import java.util.List;

public interface SectionRepository {

    List<Section> findSectionsContaining(Section sectionToAdd);

    List<Section> findSectionsByLineId(Long lineId);
}
