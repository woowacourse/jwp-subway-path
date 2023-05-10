package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.SectionDao;
import subway.domain.Sections;

@Service
public class SectionService {

    private final SectionDao sectionDao;

    public SectionService(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public void delteSections(final Sections sections) {
        sectionDao.delete(sections.getSections());
    }

    public void insertSections(final Long lineId, final Sections sections) {
        sectionDao.insert(lineId, sections.getSections());
    }
}

