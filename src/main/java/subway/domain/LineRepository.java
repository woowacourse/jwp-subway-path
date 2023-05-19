package subway.domain;

import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dto.LineEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        return new Line(insert.getId(), insert.getName(), insert.getColor());
    }

    public List<Line> findLines() {
        final List<LineEntity> lineEntities = lineDao.findAll();

        return lineEntities.stream().map(lineEntity -> {
            final List<Section> sections = sectionDao.findByLineId(lineEntity.getId());
            return convertToLine(lineEntity, sections);
        }).collect(Collectors.toList());
    }

    private Line convertToLine(final LineEntity lineEntity, final List<Section> sections) {
        final Long upEndpointId = lineEntity.getUpEndpointId();
        if (upEndpointId == null || upEndpointId == 0) {
            return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor());
        }
        final Station upEndpoint = stationDao.findById(upEndpointId);
        final SectionMap sectionMap = SectionMap.generateBySections(sections, upEndpoint);

        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), sectionMap);
    }

    public Line findLineById(final Long id) {
        final LineEntity lineEntity = lineDao.findById(id);
        final List<Section> sections = sectionDao.findByLineId(id);

        return convertToLine(lineEntity, sections);
    }

    public List<Long> saveSectionsByLine(final Line line) {
        deleteSectionsByLine(line);

        final List<Long> sectionIds = new ArrayList<>();
        line.getSections().forEach(s ->
                sectionIds.add(saveSection(line, s))
        );

        updateUpEndpoint(line);

        return sectionIds;
    }

    private void deleteSectionsByLine(final Line line) {
        sectionDao.deleteByLineId(line.getId());
    }

    private Long saveSection(final Line line, final Section section) {
        return sectionDao.insert(line.getId(), section);
    }

    private void updateUpEndpoint(final Line line) {
        final Optional<Station> upEndpoint = line.getUpEndpoint();
        if (upEndpoint.isPresent()) {
            lineDao.updateUpEndpointById(line.getId(), upEndpoint.get().getId());
            return;
        }
        lineDao.updateUpEndpointById(line.getId(), null);
    }

    public void updateLine(final Long id, final String name, final String color) {
        lineDao.update(new Line(id, name, color));
    }

    public void deleteLineById(final Long id) {
        lineDao.deleteById(id);
    }
}
