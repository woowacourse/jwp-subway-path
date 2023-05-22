package subway.application.repository;

import org.springframework.stereotype.Repository;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;

import java.util.List;

@Repository
public interface SectionRepository {
    Line findLineById(Sections sections, Long id);

    void saveAllSection(List<Section> sections, Long lineId);

    Sections findAllSectionByLineId(Long lineId);

    void deleteSections(final Long lineId, List<Section> differentSections);
}
