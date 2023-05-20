package subway.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import subway.domain.Line;
import subway.domain.Section;
import subway.dto.LineRequest;
import subway.exception.custom.LineNotExistException;

@Repository
public class LineRepository {

    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public LineRepository(final LineDao lineDao, final SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    public Line insert(final String name, final String color, final int additionalFee) {
        return lineDao.insert(Line.withNullId(name, color, additionalFee));
    }

    public List<Line> findAll() {
        return lineDao.findAll();
    }

    public List<Section> findSectionsByLineId(final Long id) {
        return sectionDao.findAllByLineId(id);
    }

    public Line findById(final Long id) {
        return lineDao.findById(id)
            .orElseThrow(() -> new LineNotExistException("노선이 존재하지 않습니다."));
    }

    public void update(final Long id, final LineRequest lineUpdateRequest) {
        lineDao.update(Line.of(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor(),
            lineUpdateRequest.getAdditionalFee()));
    }

    public void deleteById(final Long id) {
        sectionDao.deleteAllByLineId(id);
        lineDao.deleteById(id);
    }
}
