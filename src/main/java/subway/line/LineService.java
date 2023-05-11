package subway.line;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.domain.SubwayLine;
import subway.line.persistence.LineDao;
import subway.line.persistence.LineEntity;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineDao lineDao;

    public LineService(final LineDao lineDao) {
        this.lineDao = lineDao;
    }

    @Transactional
    public Long create(final SubwayLine subwayLine) {
        return lineDao.insert(subwayLine);
    }

    public LineEntity findById(final Long lineId) {
        return lineDao.findById(lineId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선 이름입니다."));
    }

    public List<LineEntity> findAll() {
        return lineDao.findAll();
    }
}
