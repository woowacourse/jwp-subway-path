package subway.persistence.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import subway.domain.section.Section;
import subway.persistence.dao.SectionDao;

@Repository
public class SectionRepository {

    private final SectionDao sectionDao;

    public SectionRepository(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public List<Section> findAll() {
        return sectionDao.findAll();
    }
}
