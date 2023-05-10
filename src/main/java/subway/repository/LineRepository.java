package subway.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.domain.line.Line;
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
        final LineEntity entity = new LineEntity(line.getId(), line.getName(), line.getColor());
        return null;
    }

    public Line findById(final Long lineId) {
        final LineEntity lineEntity = lineDao.findById(lineId)
                .orElseThrow(() -> new InvalidLineException("존재하지 않는 노선 번호 입니다."));
        final List<SectionEntity> sectionEntities = sectionDao.findAllByLineId(lineId);
        return Line.of(lineEntity, sectionEntities);
    }
}
