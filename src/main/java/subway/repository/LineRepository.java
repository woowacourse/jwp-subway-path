package subway.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;
import subway.exception.NotFoundException;

@Repository
public class LineRepository {

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public LineRepository(LineDao lineDao, StationDao stationDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public Line findLineById(Long lineId) {
        LineEntity lineEntity = lineDao.findById(lineId)
                .orElseThrow(() -> new NotFoundException("해당 노선이 존재하지 않습니다."));
        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(),
                new Sections(sectionDao.findSectionsByLineId(lineId)));
    }

    public List<Line> findAll() {
        List<LineEntity> lineEntities = lineDao.findAll();
        return lineEntities.stream()
                .map(lineEntity -> new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(),
                        new Sections(sectionDao.findSectionsByLineId(lineEntity.getId()))))
                .collect(Collectors.toList());
    }

    public Line saveLine(Line line) {
        LineEntity lineEntity = new LineEntity(line.getName(), line.getColor());
        long lineId = lineDao.insert(lineEntity);
        return new Line(lineId, line.getName(), line.getColor(), new Sections(line.getSections()));
    }

    public void updateLineInfo(Line line) {
        lineDao.update(new LineEntity(line.getId(), line.getName(), line.getColor()));
    }

    public void updateLineStation(Line line) {
        deleteOutdatedSections(line);
        addUpdatedSections(line);
    }

    public void delete(Line line) {
        lineDao.deleteById(line.getId());
    }

    private void addUpdatedSections(Line line) {
        List<Section> updatedSections = line.getSections();
        List<Section> savedSections = sectionDao.findSectionsByLineId(line.getId());
        updatedSections.removeAll(savedSections);
        updatedSections.stream()
                .map(newSection -> new SectionEntity(
                        line.getId(),
                        findStationIdByName(newSection.getUpStation().getName()),
                        findStationIdByName(newSection.getDownStation().getName()),
                        newSection.getDistance().getValue()))
                .forEach(sectionDao::insert);
    }

    private Long findStationIdByName(String stationName) {
        StationEntity stationEntity = stationDao.findByName(stationName)
                .orElseThrow(() -> new NotFoundException("해당 역이 존재하지 않습니다."));
        return stationEntity.getId();
    }

    private void deleteOutdatedSections(Line line) {
        List<Section> updatedSections = line.getSections();
        List<Section> savedSections = sectionDao.findSectionsByLineId(line.getId());
        savedSections.removeAll(updatedSections);
        savedSections.stream()
                .map(savedSection -> SectionEntity.of(line.getId(), savedSection))
                .forEach(sectionDao::delete);

    }
}
