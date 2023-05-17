package subway.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.domain.line.Line;
import subway.persistence.dao.LineDao;
import subway.persistence.dao.SectionDao;
import subway.persistence.entity.LineEntity;

@Repository
public class LineRepository {

    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public LineRepository(final LineDao lineDao, final SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    public Line insert(final Line line) {
        if (lineDao.existsByName(line.getName())) {
            throw new IllegalArgumentException("지정한 노선의 이름은 이미 존재하는 이름입니다.");
        }

        final LineEntity lineEntity = LineEntity.of(line.getName(), line.getColor());
        final LineEntity insertedLineEntity = lineDao.insert(lineEntity);

        return Line.of(insertedLineEntity.getId(), lineEntity.getName(), lineEntity.getColor());
    }

    public Optional<Line> findById(final Long id) {
        return lineDao.findById(id)
                .map(LineEntity::to);
    }

    public List<Line> findAll() {
        return lineDao.findAll()
                .stream()
                .map(LineEntity::to)
                .collect(Collectors.toList());
    }

    public void deleteById(final Long id) {
        lineDao.deleteById(id);
        sectionDao.deleteAllByLineId(id);
    }
}
