package subway.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.domain.line.Color;
import subway.domain.line.Line;
import subway.domain.line.Name;
import subway.domain.section.Sections;
import subway.entity.LineEntity;

@Repository
public class LineRepository {

    private final LineDao lineDao;

    public LineRepository(final LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public Line save(Line line) {
        LineEntity insertedLineEntity = lineDao.insert(new LineEntity(line.getName(), line.getColor()));
        return new Line(
            insertedLineEntity.getId(),
            new Name(insertedLineEntity.getName()),
            new Color(insertedLineEntity.getColor()),
            new Sections(List.of())
        );
    }
}
