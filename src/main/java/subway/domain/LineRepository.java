package subway.domain;

import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dto.LineEntity;

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

    public Line saveLine(final String name, final String color) {
        final LineEntity insert = lineDao.insert(new LineEntity(name, color));
        return new Line(name, color);
    }

    public Section saveSection(final Long lineId, final Section section) {
        sectionDao.insert(lineId, section);
        return section;
    }

    public List<Line> findLines() {
        final List<LineEntity> lineEntities = lineDao.findAll();

        return lineEntities.stream().map(lineEntity -> {
            final List<Section> sections = sectionDao.findByLineId(lineEntity.getId());
            return convertToLine(lineEntity, sections);
        }).collect(Collectors.toList());
    }

    private Line convertToLine(final LineEntity lineEntity, final List<Section> sections) {
        final Station upEndpoint = stationDao.findById(lineEntity.getUpEndpointId());
        final SectionMap sectionMap = SectionMap.generateBySections(sections, upEndpoint);

        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), sectionMap);
    }

    public Line findLineById(final Long id) {
        final LineEntity lineEntity = lineDao.findById(id);
        final List<Section> sections = sectionDao.findByLineId(id);

        return convertToLine(lineEntity, sections);
    }

    public void updateLine(final Long id, final String name, final String color) {
        lineDao.update(new Line(id, name, color));
    }

    public void deleteLineById(final Long id) {
        lineDao.deleteById(id);
    }
}
