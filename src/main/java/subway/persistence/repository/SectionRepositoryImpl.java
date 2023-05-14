package subway.persistence.repository;

import org.springframework.stereotype.Repository;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.repository.SectionRepository;
import subway.persistence.dao.SectionDao;
import subway.persistence.entity.SectionEntity;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SectionRepositoryImpl implements SectionRepository {
    private final SectionDao sectionDao;

    public SectionRepositoryImpl(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    @Override
    public void saveSection(final Long lineId, final List<Section> sections) {
        final List<SectionEntity> sectionEntities = SectionEntity.of(lineId, sections);
        sectionDao.saveSection(lineId,sectionEntities);
    }

    @Override
    public List<Section> findAllByLineId(final Long lineId) {
        return sectionDao.findAllByLineId(lineId).stream()
                .map(sectionEntity -> new Section(
                        sectionEntity.getLineId(),
                        new Station(sectionEntity.getUpStationName()),
                        new Station(sectionEntity.getDownStationName()),
                        sectionEntity.getDistance())
                ).collect(Collectors.toList());
    }
}
