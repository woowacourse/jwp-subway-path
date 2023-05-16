package subway.persistence;

import org.springframework.stereotype.Repository;
import subway.domain.Section;
import subway.persistence.dao.SectionDao;

@Repository
public class SectionRepositoryImpl implements SectionRepository {

    private final SectionDao sectionDao;

    public SectionRepositoryImpl(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    @Override
    public Section save(final Section section, final Long lineId) {
        return sectionDao.insert(section, lineId);
    }
}
