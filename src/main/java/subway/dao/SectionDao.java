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

    public Long findfirstStation() {
    }

    public Long findlastStation() {
    }

    public Section findById(Long sectionId) {
    }

    public void deleteById(Long id) {
    }
}
