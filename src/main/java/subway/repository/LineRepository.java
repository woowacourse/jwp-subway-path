package subway.repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.entity.LineEntity;
import subway.dao.entity.SectionEntity;
import subway.dao.entity.StationEntity;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.exception.DuplicateException;
import subway.exception.ErrorCode;

@Repository
public class LineRepository {
    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public LineRepository(LineDao lineDao, SectionDao sectionDao, StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public Long save(Line line) {
        duplicateLineName(line.getName());
        return lineDao.save(new LineEntity(line.getName()));
    }

    private void duplicateLineName(String name) {
        if (lineDao.isExisted(name)) {
            throw new DuplicateException(ErrorCode.DUPLICATE_NAME);
        }
    }

    public List<Line> findAll() {
        List<LineEntity> lineEntities = lineDao.findAll();

        return lineEntities.stream()
                .map(lineEntity -> findById(lineEntity.getId()))
                .collect(Collectors.toList());
    }

    public Line findById(Long lineId) {
        LineEntity lineEntity = lineDao.findById(lineId);
        return Line.of(lineEntity.getId(), lineEntity.getName(), findSectionsByLineId(lineId));
    }

    private List<Section> findSectionsByLineId(Long lineId) {
        List<SectionEntity> sectionEntities = sectionDao.findByLineId(lineId);
        Map<Long, String> stationEntities = findStations();

        return sectionEntities.stream()
                .map(sectionEntity -> new Section(
                        new Station(sectionEntity.getUpStationId(), stationEntities.get(sectionEntity.getUpStationId())),
                        new Station(sectionEntity.getDownStationId(), stationEntities.get(sectionEntity.getDownStationId())),
                        sectionEntity.getDistance()
                )).collect(Collectors.toList());
    }

    private Map<Long, String> findStations() {
        return stationDao.findAll().stream()
                .collect(Collectors.toMap(
                        StationEntity::getId,
                        StationEntity::getName)
                );
    }
}
