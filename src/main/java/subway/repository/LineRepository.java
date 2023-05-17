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
import subway.exception.InvalidSectionException;

@Repository
public class LineRepository {

    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public LineRepository(final LineDao lineDao, final SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    public Line save(final Line line) {
        final LineEntity lineEntity = lineDao.save(LineEntity.from(line));
        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), lineEntity.getFare());
    }

    public Line findById(final Long lineId) {
        final LineEntity lineEntity = lineDao.findById(lineId)
                .orElseThrow(() -> new InvalidLineException("존재하지 않는 노선 ID 입니다."));
        final List<SectionEntity> sectionEntities = sectionDao.findAllByLineId(lineId);
        return generateLine(lineEntity, sectionEntities);
    }

    private Line generateLine(final LineEntity lineEntity, final List<SectionEntity> sectionEntities) {
        final Line line = new Line(
                lineEntity.getId(),
                lineEntity.getName(),
                lineEntity.getColor(),
                lineEntity.getFare()
        );
        loadSections(line, generateSections(sectionEntities));
        return line;
    }

    private List<Section> generateSections(final List<SectionEntity> sectionEntities) {
        return sectionEntities.stream()
                .map(Section::from)
                .collect(Collectors.toList());
    }

    private void loadSections(final Line line, final List<Section> sections) {
        while (!sections.isEmpty()) {
            final Section section = sections.remove(0);
            try {
                line.addSection(section.getUpward(), section.getDownward(), section.getDistance());
            } catch (InvalidSectionException e) {
                sections.add(section);
            }
        }
    }

    public List<Line> findAll() {
        return lineDao.findAll()
                .stream()
                .map(lineEntity -> generateLine(lineEntity, sectionDao.findAllByLineId(lineEntity.getId())))
                .collect(Collectors.toList());
    }

    public void update(final Line line) {
        lineDao.update(LineEntity.from(line));
        sectionDao.deleteAllByLineId(line.getId());
        final List<SectionEntity> entities = generateSectionEntities(line);
        sectionDao.saveAll(entities);
    }

    private List<SectionEntity> generateSectionEntities(final Line line) {
        return line.getSections()
                .stream()
                .map(section -> SectionEntity.of(line.getId(), section))
                .collect(Collectors.toUnmodifiableList());
    }
}
