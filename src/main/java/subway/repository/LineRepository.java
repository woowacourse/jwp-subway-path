package subway.repository;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.Subway;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;
import subway.exception.DuplicatedNameException;
import subway.repository.dao.LineDao;
import subway.repository.dao.SectionDao;
import subway.repository.dao.StationDao;

@Repository
public class LineRepository {

    private final SectionDao sectionDao;
    private final LineDao lineDao;
    private final StationDao stationDao;

    public LineRepository(SectionDao sectionDao, LineDao lineDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
        this.stationDao = stationDao;
    }

    public Long save(Line line) {
        if (lineDao.existsByName(line.getName())) {
            throw new DuplicatedNameException(line.getName());
        }
        LineEntity newLineEntity = lineDao.insert(new LineEntity(line.getName()));

        List<StationEntity> stations = StationEntity.of(line);
        stationDao.insertAll(stations);

        saveSections(newLineEntity.getId(), line);

        return newLineEntity.getId();
    }

    public List<Line> findAll() {
        List<LineEntity> lineEntities = lineDao.findAll();

        Map<Long, StationEntity> collect = stationDao.findAll().stream()
                .collect(toMap(StationEntity::getId, stationEntity -> stationEntity));

        Map<Long, List<SectionEntity>> idBySections = sectionDao.findAll().stream()
                .collect(Collectors.groupingBy(SectionEntity::getLineId));

        return lineEntities.stream()
                .map(lineEntity -> toLine(lineEntity, idBySections.getOrDefault(lineEntity.getId(), new ArrayList<>()),
                        collect))
                .collect(toList());
    }

    private Line toLine(final LineEntity lineEntity, final List<SectionEntity> sectionEntities,
                        Map<Long, StationEntity> idByStation) {
        List<Section> sections = sectionEntities.stream()
                .map(sectionEntity -> new Section(
                        idByStation.get(sectionEntity.getSourceStationId()).getName(),
                        idByStation.get(sectionEntity.getTargetStationId()).getName(),
                        sectionEntity.getDistance()
                ))
                .collect(toList());

        return new Line(lineEntity.getName(), sections);
    }

    public void updateLine(Long lineId, Line updatedLine) {
        sectionDao.deleteAll(lineId);
        saveSections(lineId, updatedLine);
    }

    private void saveSections(Long lineId, Line line) {
        for (Section section : line.getSections()) {
            Station source = section.getSource();
            Station target = section.getTarget();
            StationEntity sourceStationEntity = stationDao.findByName(source.getName())
                    .orElseThrow(() -> new NoSuchElementException());
            StationEntity targetStationEntity = stationDao.findByName(target.getName())
                    .orElseThrow(() -> new NoSuchElementException());

            sectionDao.insert(new SectionEntity(sourceStationEntity.getId(), targetStationEntity.getId(),
                    lineId, section.getDistance()));
        }
    }

    public Line findById(Long id) {
        Subway subway = new Subway(findAll());
        LineEntity lineEntity = lineDao.findById(id);
        return subway.findLineByName(lineEntity.getName());
    }
}
