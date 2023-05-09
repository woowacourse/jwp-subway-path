package subway.dao;

import java.util.List;

import subway.domain.Section;

public class SectionDao {
    public Long insert(Section section) {
        return 1L;
    }

    public List<Section> findAllSectionByLineId(Long lineId) {
        return List.of();
    }
}
