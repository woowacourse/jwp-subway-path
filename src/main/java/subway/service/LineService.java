package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.Entity.LineEntity;
import subway.domain.line.Line;
import subway.dto.request.LineRequest;
import subway.dto.response.LineResponse;
import subway.persistence.dao.LineDao;

@Service
@Transactional
public class LineService {
    private final LineDao lineDao;

    public LineService(LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public LineResponse saveLine(LineRequest request) {
        LineEntity lineEntity = lineDao.insert(Line.of(request.getName(), request.getColor(), request.getExtraFare()));
        return LineResponse.of(lineEntity);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        LineEntity lineEntity = new LineEntity(
                id,
                lineUpdateRequest.getName(),
                lineUpdateRequest.getColor(),
                lineUpdateRequest.getExtraFare()
        );
        lineDao.update(lineEntity);
    }

    public void deleteLineById(Long id) {
        lineDao.deleteById(id);
    }

}
