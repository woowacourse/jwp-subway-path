package subway.line.service;

import org.springframework.stereotype.Service;
import subway.line.dao.LineDao;
import subway.line.domain.Line;
import subway.line.dto.LineRequest;
import subway.line.dto.LineResponse;

import java.util.List;
import java.util.Optional;

@Service
public class LineService {

    private final LineDao lineDao;

    public LineService(final LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public List<Line> findAllLine() {
        return lineDao.findAll();
    }

    public Line findLineById(Long id) {
        return lineDao.findById(id);
    }

    public LineResponse saveLine(final LineRequest request) {
        Optional<Line> line = lineDao.findByName(request.getName());
        if (line.isPresent()) {
            throw new IllegalArgumentException("노선 이름이 이미 존재합니다. 유일한 노선 이름을 사용해주세요.");
        }
        final Line findLine = lineDao.insert(new Line(request.getName(), request.getColor()));
        return LineResponse.of(findLine);
    }

    public void updateLine(final Long id, final LineRequest lineUpdateRequest) {
        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(final Long id) {
        lineDao.deleteById(id);
    }
}
