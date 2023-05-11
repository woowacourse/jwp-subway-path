package subway.dao.line;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.section.SectionDao;
import subway.dao.section.SectionEntity;
import subway.domain.line.Line;
import subway.domain.line.LineColor;
import subway.domain.line.LineName;
import subway.domain.line.LineRepository;
import subway.domain.section.Section;
import subway.domain.section.Sections;


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

    private Sections toSections(final List<SectionEntity> sectionEntities) {
        final List<Section> sections = sectionEntities.stream()
                .map(SectionEntity::toSection)
                .collect(Collectors.toList());
        
        return new Sections(sections);
    }
}
