package subway.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import subway.domain.Section;

@Repository
public class SectionDao {
    public Long insert(Section section) {
        return 1L;
    }

    public List<Section> findAllSectionByLineId(Long lineId) {
        return List.of();
    }

    public Long findFirstStation() {
        return null;
    }

    public Long findLastStation() {
        return null;
    }

    public Section findById(Long sectionId) {
        return null;
    }

    public void deleteById(Long id) {
    }
}
