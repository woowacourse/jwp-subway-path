package subway.line.adapter.output.persistence;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.section.adapter.output.persistence.SectionDao;
import subway.section.adapter.output.persistence.SectionEntity;
import subway.line.domain.Line;
import subway.line.domain.LineColor;
import subway.line.domain.LineName;
import subway.line.application.port.output.LineRepository;
import subway.section.domain.Section;
import subway.section.domain.Sections;


@Repository
public class LineRepositoryImpl implements LineRepository {

    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public LineRepositoryImpl(final LineDao lineDao, final SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    @Override
    public Line findById(final Long id) {
        final LineEntity lineEntity = lineDao.findById(id);
        final List<SectionEntity> sectionEntities = sectionDao.findByLineId(id);

        return new Line(
                lineEntity.getLineId(),
                toSections(sectionEntities),
                new LineName(lineEntity.getName()),
                new LineColor(lineEntity.getColor()));
    }

    @Override
    public List<Line> findAll() {
        final List<LineEntity> allLineEntities = lineDao.findAll();

        return allLineEntities.stream()
                .map(this::toLine) //FIXME: N + 1
                .collect(Collectors.toList());
    }

    private Line toLine(final LineEntity lineEntity) {
        final List<SectionEntity> sectionEntities = sectionDao.findByLineId(lineEntity.getLineId());
        final Sections sections = toSections(sectionEntities);
        return new Line(sections, new LineName(lineEntity.getName()), new LineColor(lineEntity.getColor()));
    }

    private Sections toSections(final List<SectionEntity> sectionEntities) {
        final List<Section> sections = sectionEntities.stream()
                .map(SectionEntity::toSection)
                .collect(Collectors.toList());

        return new Sections(sections);
    }
}
