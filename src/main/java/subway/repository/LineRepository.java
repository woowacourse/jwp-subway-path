package subway.repository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.entity.LineEntity;
import subway.dao.entity.SectionEntity;
import subway.dao.entity.StationEntity;
import subway.domain.*;
import subway.exception.DataConstraintViolationException;
import subway.exception.DuplicatedDataException;
import subway.exception.NotFoundDataException;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class LineRepository {

    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final LineDao lineDao;

    public LineRepository(final StationDao stationDao, final SectionDao sectionDao, final LineDao lineDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
    }

    public Line save(final Line line) {
        LineEntity lineEntity = LineEntity.from(line);
        validateDuplicate(lineEntity);
        Long savedId = lineDao.save(lineEntity);
        return new Line(
                savedId,
                new LineName(line.getName()),
                new LineColor(line.getColor()),
                Sections.create()
        );
    }

    public List<Line> findAll() {
        List<LineEntity> allLines = lineDao.findAll();
        Map<Long, Station> allStationsById = stationDao.findAll().stream()
                .map(StationEntity::convertToStation)
                .collect(Collectors.toMap(Station::getId, station -> station));
        Map<Long, List<Section>> allSectionsByLindId = getAllSectionsByLineId(allStationsById);
        return allLines.stream()
                .map(lineEntity -> lineEntity.convertToLine(allSectionsByLindId.getOrDefault(lineEntity.getId(), new ArrayList<>())))
                .collect(Collectors.toList());
    }

    public Line findById(final Long id) {
        LineEntity lineEntity = lineDao.findById(id).orElseThrow(() -> new NotFoundDataException("해당 라인이 없습니다."));
        Map<Long, Station> allStationsById = stationDao.findAll().stream()
                .map(StationEntity::convertToStation)
                .collect(Collectors.toMap(Station::getId, station -> station));
        Map<Long, List<Section>> allSectionsByLindId = getAllSectionsByLineId(allStationsById);
        return lineEntity.convertToLine(allSectionsByLindId.getOrDefault(lineEntity.getId(), new ArrayList<>()));
    }

    private Map<Long, List<Section>> getAllSectionsByLineId(final Map<Long, Station> allStationsById) {
        Map<Long, List<Section>> allSectionsByLindId = new HashMap<>();
        List<SectionEntity> allSections = sectionDao.findAll();
        for (SectionEntity sectionsEntity : allSections) {
            Long lineId = sectionsEntity.getLineId();
            List<Section> sections = allSectionsByLindId.getOrDefault(lineId, new ArrayList<>());
            sections.add(sectionsEntity.convertToSection(allStationsById));
            allSectionsByLindId.put(lineId, sections);
        }
        return allSectionsByLindId;
    }

    public void update(final Line updateLine) {
        LineEntity lineEntity = LineEntity.from(updateLine);
        validateExist(lineEntity);
        lineDao.update(lineEntity);
    }

    public void delete(final Line line) {
        LineEntity lineEntity = LineEntity.from(line);
        validateExist(lineEntity);
        try {
            lineDao.delete(lineEntity);
        } catch (DataIntegrityViolationException e) {
            throw new DataConstraintViolationException("해당 라인은 삭제할 수 없습니다.");
        }
    }

    private void validateExist(final LineEntity lineEntity) {
        Optional<LineEntity> maybeLine = lineDao.findById(lineEntity.getId());
        if (maybeLine.isEmpty()) {
            throw new NotFoundDataException("해당 라인은 존재하지 않습니다");
        }
    }

    private void validateDuplicate(final LineEntity lineEntity) {
        List<LineEntity> maybeLineEntity = lineDao.findByNameOrColor(lineEntity);
        if (!maybeLineEntity.isEmpty()) {
            throw new DuplicatedDataException("이미 존재하는 라인이 있습니다.");
        }
    }
}
