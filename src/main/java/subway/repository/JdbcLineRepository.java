package subway.repository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.entity.LineEntity;
import subway.entity.SectionStationEntity;

@Repository
public class JdbcLineRepository implements LineRepository {

    private static final String NO_SUCH_ID_MESSAGE = "해당하는 ID가 없습니다.";

    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public JdbcLineRepository(final LineDao lineDao, final SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    @Override
    public Long save(final Line line) {
        return lineDao.insert(LineEntity.from(line));
    }

    @Override
    public Line findById(final Long id) {
        final LineEntity lineEntity = lineDao.findById(id)
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_ID_MESSAGE));
        final List<Section> sections = sectionDao.findByLineId(id).stream()
                .map(SectionStationEntity::toDomain)
                .collect(Collectors.toList());
        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), new Sections(sections));
    }

    @Override
    public List<Line> findAll() {
        final List<LineEntity> lineEntities = lineDao.findAll();
        return lineEntities.stream()
                .map(it -> findById(it.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public void update(final Long id, final Line line) {
        lineDao.update(id, LineEntity.from(line));
    }

    @Override
    public void deleteById(final Long id) {
        lineDao.deleteById(id);
    }

    @Override
    public boolean notExistsById(final Long id) {
        return lineDao.notExistsById(id);
    }
}
