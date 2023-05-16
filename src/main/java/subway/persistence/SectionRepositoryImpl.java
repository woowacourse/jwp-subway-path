package subway.persistence;

import org.springframework.stereotype.Repository;
import subway.domain.Section;
import subway.persistence.dao.SectionDao;
import subway.persistence.entity.SectionEntity;

@Repository
public class SectionRepositoryImpl implements SectionRepository {

    private final SectionDao sectionDao;

    public SectionRepositoryImpl(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    @Override
    public Section save(final Section section, final Long lineId) {
        final SectionEntity sectionEntity = SectionEntity.from(section);
        final SectionEntity savedSectionEntity = sectionDao.insert(sectionEntity, lineId);
        return new Section(savedSectionEntity.getId(), section.getUpStation(),
            section.getDownStation(), section.getDistance());
    }
}
