package subway.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import subway.dao.GeneralSectionDao;
import subway.domain.section.general.GeneralSection;
import subway.exception.SectionNotFoundException;

@Repository
public class GeneralSectionRepository {

    private final GeneralSectionDao generalSectionDao;

    public GeneralSectionRepository(final GeneralSectionDao generalSectionDao) {
        this.generalSectionDao = generalSectionDao;
    }

    public GeneralSection saveSection(GeneralSection sectionToSave) {
        return generalSectionDao.insert(sectionToSave);
    }

    public List<GeneralSection> findAll() {
        return generalSectionDao.selectAll();
    }

    public List<GeneralSection> findAllSectionByLineId(Long lineId) {
        return generalSectionDao.selectAllSectionByLineId(lineId);
    }

    public void removeSectionById(Long sectionId) {
        if (generalSectionDao.isNotExistById(sectionId)) {
            throw new SectionNotFoundException("구간 ID에 해당하는 구간이 존재하지 않습니다.");
        }
        generalSectionDao.deleteById(sectionId);
    }
}
