package subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Section;
import subway.domain.Sections;
import subway.persistence.dao.SectionDao;

@Service
public class SectionService {

    private final SectionDao sectionDao;

    public SectionService(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public List<Section> findByLineId(final Long lineId) {
        return sectionDao.findByLineId(lineId);
    }

    @Transactional
    public void deleteSections(final Sections sections) {
        sectionDao.delete(sections.getSections());
    }

    @Transactional
    public void insertSections(final Long lineId, final Sections sections) {
        sectionDao.insert(lineId, sections.getSections());
    }
}

