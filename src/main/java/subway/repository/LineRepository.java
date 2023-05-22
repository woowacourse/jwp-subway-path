package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.LineEntity;
import subway.dao.SectionDao;
import subway.dao.SectionEntity;
import subway.dao.StationDao;
import subway.domain.line.Line;
import subway.domain.section.Section;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class LineRepository {

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public LineRepository(final LineDao lineDao, final SectionDao sectionDao, final StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public Line save(final Line line) {
        final LineEntity lineEntity = LineEntity.from(line);
        final List<Section> sections = findSectionsByLineId(lineEntity.getId());
        return lineDao.insert(lineEntity).toLine(sections);
    }

    public List<Line> findLines() {
        final List<LineEntity> lineEntities = lineDao.findAll();
        return lineEntities.stream()
                .map(lineEntity -> lineEntity.toLine(findSectionsByLineId(lineEntity.getId())))
                .collect(Collectors.toUnmodifiableList());
    }

    private List<Section> findSectionsByLineId(final Long lineId) {
        return sectionDao.findSectionsByLineId(lineId)
                .stream()
                .map(this::generateSection)
                .collect(Collectors.toUnmodifiableList());
    }

    private Section generateSection(final SectionEntity sectionEntity) {
        return new Section(
                sectionEntity.getId(),
                stationDao.findById(sectionEntity.getFromId()).toStation(),
                stationDao.findById(sectionEntity.getToId()).toStation(),
                sectionEntity.getDistance());
    }

    public Line findLineById(final Long id) {
        final LineEntity lineEntity = lineDao.findById(id);
        final List<Section> sections = findSectionsByLineId(lineEntity.getId());
        return lineEntity.toLine(sections);
    }

    public boolean contains(final Line line) {
        return lineDao.findAll().stream()
                .anyMatch(it -> it.getName().equals(line.getNameValue()));
    }
}
