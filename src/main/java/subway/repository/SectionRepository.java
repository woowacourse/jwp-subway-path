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

    public void deleteAllByLineId(final Long lineId) {

        sectionDao.deleteAllByLineId(lineId);
    }

    public void insertAllByLineId(final Long lineId, final Sections sections) {

        sectionDao.insertAllByLineId(lineId, sections.getSections());
    }
}
