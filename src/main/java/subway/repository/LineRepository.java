package subway.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.exception.InvalidLineException;

@Repository
public class LineRepository {

    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public LineRepository(final LineDao lineDao, final SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    public Line save(final Line line) {
        final LineEntity entity = lineDao.save(new LineEntity(line.getId(), line.getName(), line.getColor()));
        return Line.from(entity);
    }

    public Line findById(final Long lineId) {
        final LineEntity lineEntity = lineDao.findById(lineId)
                .orElseThrow(() -> new InvalidLineException("존재하지 않는 노선 ID 입니다."));
        final List<SectionEntity> sectionEntities = sectionDao.findAllByLineId(lineId);
        return Line.of(lineEntity, sectionEntities);
    }

    public List<Line> findAll() {
        return lineDao.findAll()
                .stream()
                .map(lineEntity -> Line.of(lineEntity, sectionDao.findAllByLineId(lineEntity.getId())))
                .collect(Collectors.toList());
    }

    public void update(final Line line) {
        lineDao.update(new LineEntity(line.getId(), line.getName(), line.getColor()));
        sectionDao.deleteAllByLineId(line.getId());
        final List<SectionEntity> entities = generateSectionEntities(line);
        sectionDao.saveAll(entities);
    }

    private List<SectionEntity> generateSectionEntities(final Line line) {
        final List<Section> sections = line.getSections();
        sections.removeIf(section -> section.getDownward() == Station.TERMINAL);
        return sections.stream()
                .map(section -> new SectionEntity(
                        line.getId(),
                        section.getUpward().getId(),
                        section.getDownward().getId(),
                        section.getDistance()))
                .collect(Collectors.toUnmodifiableList());
    }
}
