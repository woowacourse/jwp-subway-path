package subway.application;

import java.util.Collections;
import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.domain.line.Line;
import subway.dto.LineRequest;
import subway.dto.LineResponse;

@Service
public class LineService {
    private final LineDao lineDao;

    public LineService(LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public LineResponse saveLine(LineRequest request) {
        Line persistLine = lineDao.insert(new Line(request.getName(), request.getColor()));
        return LineResponse.of(persistLine.getId(), persistLine.getName(), persistLine.getColor(), Collections.emptyList());
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineDao.deleteById(id);
    }
}
