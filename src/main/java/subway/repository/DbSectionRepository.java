package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Section;
import subway.domain.Station;
import subway.entity.SectionEntity;

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
        sectionDao.save(sectionEntity);
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
}
