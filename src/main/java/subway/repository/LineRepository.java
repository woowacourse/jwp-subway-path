package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.*;
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

    public Line save(final LineEntity lineEntity) {
        return lineDao.insert(lineEntity).toLine();
    }

    public List<Line> findLines() {
        final List<LineEntity> lineEntities = lineDao.findAll();
        return lineEntities.stream()
                .map(this::generateLine)
                .collect(Collectors.toUnmodifiableList());
    }

    private Line generateLine(final LineEntity lineEntity) {
        return lineEntity.toLine(findSectionsByLineId(lineEntity.getId()));
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
        return generateLine(lineDao.findById(id));
    }
}
