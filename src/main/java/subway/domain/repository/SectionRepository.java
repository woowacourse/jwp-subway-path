package subway.domain.repository;

import subway.domain.Section;

import java.util.List;

public interface SectionRepository {

    void saveSection(Long lineId, List<Section> sections);

    List<Section> findAllByLineId(Long lineId);
}
