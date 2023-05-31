package subway.line.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import subway.line.dao.LineDao;
import subway.line.dao.SectionDao;
import subway.line.domain.Line;
import subway.line.domain.Lines;
import subway.line.entity.LineEntity;
import subway.line.entity.SectionEntity;
import subway.station.dao.StationDao;
import subway.station.entity.StationEntity;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@Repository
public class LineRepository {

    private final StationDao stationDao;
    private final LineDao lineDao;
    private final SectionDao sectionDao;

    @Autowired
    public LineRepository(StationDao stationDao, LineDao lineDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    public long createLine(Line lineToCreate) {
        lineDao.findLineByName(lineToCreate.getName())
               .ifPresent(ignored -> {
                   throw new IllegalStateException("디버깅: 이미 노선 이름에 해당하는 노선이 존재합니다." + lineToCreate.getName());
               });

        final long lineId = lineDao.insert(EntityMapper.toLineEntity(lineToCreate));
        sectionDao.insertSections(EntityMapper.toSectionEntities(lineToCreate, lineId));

        return lineId;
    }

    public long updateLine(Line line) {
        LineEntity lineEntity = lineDao.findLineByName(line.getName())
                                       .orElseThrow(() -> new NoSuchElementException("디버깅: 노선 이름에 해당하는 노선이 없습니다. line이름: " + line.getName()));

        sectionDao.deleteSectionsByLineId(lineEntity.getId());
        sectionDao.insertSections(EntityMapper.toSectionEntities(line, lineEntity.getId()));

        return lineEntity.getId();
    }

    public Optional<Line> findLineById(Long lineId) {
        List<SectionEntity> sectionEntitiesOfLine = sectionDao.findSectionsByLineId(lineId);
        final List<StationEntity> allStationEntities = stationDao.findAllStations();

        return lineDao.findLineById(lineId)
                      .map(lineEntity -> EntityMapper.toLine(lineEntity, sectionEntitiesOfLine, allStationEntities));
    }

    public Lines findAllLines() {
        final List<StationEntity> allStationEntities = stationDao.findAllStations();

        return lineDao.findAll()
                      .stream()
                      .map(lineEntity -> {
                          final List<SectionEntity> sectionEntitiesOfLine = sectionDao.findSectionsByLineId(lineEntity.getId());

                          return EntityMapper.toLine(lineEntity, sectionEntitiesOfLine, allStationEntities);
                      })
                      .collect(collectingAndThen(toList(), Lines::new));
    }

    public void deleteLine(Line lineToDelete) {
        lineDao.deleteLineById(lineToDelete.getId());
    }
}
