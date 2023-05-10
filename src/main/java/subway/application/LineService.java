package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.line.LineDao;
import subway.domain.entity.LineEntity;
import subway.dto.line.LineRequest;

import java.util.List;

@Service
public class LineService {

    private final LineDao lineDao;

    public LineService(final LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public LineEntity findLineByName(final String lineName) {
        return lineDao.findByName(lineName);
    }

    public Long saveLine(final LineRequest request) {
        return lineDao.insert(new LineEntity(null, request.getLineNumber(), request.getName(), request.getColor()));
    }

    public List<LineEntity> findAll() {
        return lineDao.findAll();
    }

    public void deleteLineById(final Long id) {
        lineDao.deleteById(id);
    }

    public LineEntity findByLineNumber(final Long lineNumber) {
        return lineDao.findByLineNumber(lineNumber);
    }
}
