package subway.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.domain.line.Line;
import subway.exception.LineNotFoundException;

@Repository
public class LineRepository {

    private final LineDao lineDao;

    public LineRepository(final LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public Line saveLine(Line lineToSave) {
        return lineDao.insert(lineToSave);
    }

    public Line findLineById(Long lineId) {
        return lineDao.selectById(lineId)
                .orElseThrow(() -> new LineNotFoundException("노선 ID에 해당하는 노선이 존재하지 않습니다."));
    }

    public List<Line> findAllLine() {
        return lineDao.selectAll();
    }

    public void removeLineById(Long lineId) {
        if (lineDao.isNotExistById(lineId)) {
            throw new LineNotFoundException("노선 ID에 해당하는 노선이 존재하지 않습니다.");
        }
        lineDao.deleteById(lineId);
    }
}
