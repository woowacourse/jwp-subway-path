package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.entity.SectionEntity;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DbSectionRepository implements SectionRepository {

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public DbSectionRepository(final LineDao lineDao, final StationDao stationDao, final SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    @Override
    public void save(final Long lineId, final Section section) {
        final SectionEntity sectionEntity = new SectionEntity(
                lineId,
                section.getUpStation().getId(),
                section.getDownStation().getId(),
                section.getDistance());
        sectionDao.insert(sectionEntity);
    }

    @Override
    public void delete(final Section section) {

    }

    @Override
    public void deleteSection(final Long lineId, final Station previousStation, final Long deletedStationId) {
        final Long previousStationId = stationDao.findBy(previousStation.getName()).getId();
        sectionDao.delete(lineId, previousStationId, deletedStationId);
    }

    @Override
    public void deleteSection(final Long lineId, final Long deletedStationId, final Station nextStation) {
        final Long nextStationId = stationDao.findBy(nextStation.getName()).getId();
        sectionDao.delete(lineId, deletedStationId, nextStationId);
    }

    @Override
    public List<Section> findAllSectionOf(final Line line) {
        final List<SectionEntity> sectionEntities = sectionDao.findBy(line.getId());
        return sectionEntities.stream()
                .map(sectionEntity -> new Section(
                        Station.from(stationDao.findBy(sectionEntity.getUpStationId())),
                        Station.from(stationDao.findBy(sectionEntity.getDownStationId())),
                        sectionEntity.getDistance()))
                .collect(Collectors.toList());
    }
}
