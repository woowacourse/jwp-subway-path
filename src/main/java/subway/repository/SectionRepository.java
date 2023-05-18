package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.SectionDao;
import subway.domain.section.Sections;

@Repository
public class SectionRepository {

    private final SectionDao sectionDao;

    public SectionRepository(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public void save(final Long lineId, final Sections sections) {
        sectionDao.deleteAllByLineId(lineId);
        sectionDao.insertAllByLineId(lineId, sections.getSections());
    }
}
