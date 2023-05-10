package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.line.LineDao;
import subway.domain.entity.LineEntity;
import subway.dto.line.LineRequest;

import java.util.List;

@Service
public class LineService {

    private final LineDao lineDao;

    public LineService(LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public LineEntity findLineByName(final String lineName) {
        return lineDao.findByName(lineName);
    }

    public Long saveLine(LineRequest request) {
        return lineDao.insert(new LineEntity(null, request.getName(), request.getColor()));
    }

    public List<LineEntity> findLineResponses() {
        return lineDao.findAll();
    }

    public LineEntity findLineById(final Long id) {
        return lineDao.findById(id);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
//        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineDao.deleteById(id);
    }
}
