package subway.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.domain.line.Line;
import subway.domain.section.Section;
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
        final LineEntity entity = lineDao.save(new LineEntity(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getExtraFare()
        ));
        return new Line(entity.getId(), entity.getName(), entity.getColor(), entity.getExtraFare());
    }

    public Line findById(final Long lineId) {
        final LineEntity lineEntity = lineDao.findById(lineId)
                .orElseThrow(() -> new InvalidLineException("존재하지 않는 노선 ID 입니다."));
        final List<SectionEntity> sectionEntities = sectionDao.findAllByLineId(lineId);
        return generateLine(lineEntity, sectionEntities);
    }

    public List<Line> findAll() {
        return lineDao.findAll()
                .stream()
                .map(lineEntity -> generateLine(lineEntity, sectionDao.findAllByLineId(lineEntity.getId())))
                .collect(Collectors.toList());
    }

    public void update(final Line line) {
        lineDao.update(new LineEntity(line.getId(), line.getName(), line.getColor(), line.getExtraFare()));
        sectionDao.deleteAllByLineId(line.getId());
        final List<SectionEntity> entities = generateSectionEntities(line);
        sectionDao.saveAll(entities);
    }

    private Line generateLine(final LineEntity lineEntity, final List<SectionEntity> sectionEntities) {
        return new Line(
                lineEntity.getId(),
                lineEntity.getName(),
                lineEntity.getColor(),
                lineEntity.getExtraFare(),
                generateSections(sectionEntities)
        );
    }

    private List<Section> generateSections(final List<SectionEntity> sectionEntities) {
        return sectionEntities.stream()
                .map(Section::from)
                .collect(Collectors.toList());
    }

    private List<SectionEntity> generateSectionEntities(final Line line) {
        final List<Section> sections = line.getSections();
        return sections.stream()
                .map(section -> new SectionEntity(
                        line.getId(),
                        section.getUpward().getId(),
                        section.getDownward().getId(),
                        section.getDistance()))
                .collect(Collectors.toUnmodifiableList());
    }
}
