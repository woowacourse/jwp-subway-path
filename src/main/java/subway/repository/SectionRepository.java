package subway.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import subway.dao.SectionDao;
import subway.domain.section.Section;
import subway.exception.SectionNotFoundException;

@Repository
public class SectionRepository {

    private final SectionDao sectionDao;

    public SectionRepository(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public Section saveSection(Section sectionToSave) {
        return sectionDao.insert(sectionToSave);
    }

    public List<Section> findAllSectionByLineId(Long lineId) {
        return sectionDao.selectAllSectionByLineId(lineId);
    }

    public void removeSectionById(Long sectionId) {
        if (sectionDao.isNotExistById(sectionId)) {
            throw new SectionNotFoundException("구간 ID에 해당하는 구간이 존재하지 않습니다.");
        }
        sectionDao.deleteById(sectionId);
    }
}
