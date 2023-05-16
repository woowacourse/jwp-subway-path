package subway.repository;

import static java.util.stream.Collectors.toList;

import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.SectionEntity;
import java.util.List;

@Repository
public class SectionRepository {

    private final StationDao stationDao;
    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public SectionRepository(final StationDao stationDao, final LineDao lineDao, final SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    public Section save(final Section section) {
        SectionEntity sectionEntity = new SectionEntity(
                section.getLine().getId(),
                section.getUpStation().getId(),
                section.getDownStation().getId(),
                section.getDistance().getValue()
        );

        SectionEntity savedEntity = sectionDao.insert(sectionEntity);

        return toDomain(savedEntity);
    }

    public List<Section> findAll() {
        List<SectionEntity> sectionEntities = sectionDao.findAll();

        return sectionEntities.stream()
                .map(this::toDomain)
                .collect(toList());
    }

    public void delete(final Section section) {
        sectionDao.deleteById(section.getId());
    }

    private Section toDomain(final SectionEntity sectionEntity) {
        return new Section(
                sectionEntity.getId(),
                lineDao.findById(sectionEntity.getLineId()),
                stationDao.findById(sectionEntity.getUpStationId()),
                stationDao.findById(sectionEntity.getDownStationId()),
                new Distance(sectionEntity.getDistance())
        );
    }
}
