package subway.persistence.repository;

import org.springframework.stereotype.Repository;
import subway.persistence.dao.LineDao;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;
import subway.persistence.entity.LineEntity;
import subway.persistence.entity.SectionEntity;
import subway.persistence.entity.StationEntity;

import java.util.List;

@Repository
public class JdbcLineRepository implements LineRepository {

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public JdbcLineRepository(final LineDao lineDao, final SectionDao sectionDao, final StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    @Override
    public LineEntity saveLine(final LineEntity lineEntity) {
        return lineDao.insert(lineEntity);
    }

    @Override
    public LineEntity findLineById(final Long id) {
        return lineDao.findById(id);
    }

    @Override
    public List<SectionEntity> findSectionsByLine(final LineEntity lineEntity) {
        return sectionDao.findAllByLineId(lineEntity.getId());
    }

    @Override
    public StationEntity findStationById(final Long id) {
        return stationDao.findById(id);
    }

    @Override
    public StationEntity findStationByName(final String stationName) {
        return stationDao.findByName(stationName);
    }

    @Override
    public List<LineEntity> findAllLines() {
        return lineDao.findAll();
    }

    @Override
    public void updateLine(LineEntity lineEntity) {
        lineDao.update(lineEntity);
    }

    @Override
    public void deleteLineById(final Long id) {
        lineDao.deleteById(id);
    }
}
