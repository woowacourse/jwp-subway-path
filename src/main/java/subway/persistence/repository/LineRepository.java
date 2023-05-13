package subway.persistence.repository;

import org.springframework.stereotype.Repository;
import subway.domain.Line;
import subway.persistence.dao.LineDao;
import subway.persistence.dao.SectionDao;
import subway.persistence.entity.LineEntity;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class LineRepository {

    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public LineRepository(final LineDao lineDao, final SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    public Line insert(final Line line) {
        final LineEntity lineEntity = LineEntity.of(line.getName(), line.getColor());
        final LineEntity insertedLineEntity = lineDao.insert(lineEntity);

        return Line.of(insertedLineEntity.getId(), lineEntity.getName(), lineEntity.getColor());
    }

    public Line findById(final Long id) {
        return lineDao.findById(id)
                .to();
    }

    public List<Line> findAll() {
        return lineDao.findAll()
                .stream()
                .map(LineEntity::to)
                .collect(Collectors.toList());
    }

    public void deleteById(final Long id) {
        lineDao.deleteById(id);
        sectionDao.deleteByLineId(id);
    }
}
