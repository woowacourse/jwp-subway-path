package subway.dao.section;

import org.springframework.stereotype.Repository;
import subway.domain.section.Section;
import subway.domain.section.SectionRepository;

@Repository
public class SectionRepositoryImpl implements SectionRepository {

    private final SectionDao sectionDao;

    public SectionRepositoryImpl(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    @Override
    public Long save(final Section section, final Long lineId) {
        final SectionEntity sectionEntity = new SectionEntity(
                section.getFirstStation().getStationId(),
                section.getSecondStation().getStationId(),
                section.getDistance().getDistance(),
                lineId
        );
        final SectionEntity insert = sectionDao.insert(sectionEntity);

        return insert.getId();
    }

    @Override
    public void delete(final Section removedSection) {
        sectionDao.deleteByStations(
                removedSection.getFirstStation().getStationId(),
                removedSection.getSecondStation().getStationId()
        );
    }
}