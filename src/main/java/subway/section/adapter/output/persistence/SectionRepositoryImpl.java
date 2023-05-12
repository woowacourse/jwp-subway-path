package subway.section.adapter.output.persistence;

import org.springframework.stereotype.Repository;
import subway.section.domain.Section;
import subway.section.application.port.output.SectionRepository;

@Repository
public class SectionRepositoryImpl implements SectionRepository {

    private final SectionDao sectionDao;

    public SectionRepositoryImpl(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    @Override
    public Long save(final Section section, final Long lineId) {
        final SectionEntity sectionEntity = new SectionEntity(
                section.getFirstStation().getStationName(),
                section.getSecondStation().getStationName(),
                section.getDistance().getDistance(),
                lineId
        );
        final SectionEntity insert = sectionDao.insert(sectionEntity);

        return insert.getId();
    }

    @Override
    public void delete(final Section removedSection) {
        sectionDao.deleteByStations(
                removedSection.getFirstStation().getStationName(),
                removedSection.getSecondStation().getStationName()
        );
    }
}
