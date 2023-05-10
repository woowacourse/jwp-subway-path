package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.entity.Line;

@Transactional(readOnly = true)
@Service
public class LineService {
    private final LineDao lineDao;

    public LineService(LineDao lineDao) {
        this.lineDao = lineDao;
    }

    @Transactional
    public Long saveLine(LineRequest request) {
        return lineDao.insert(Line.of(request.getName(), request.getColor()));
    }

    public List<LineResponse> findLineResponses() {
        List<Line> persistLines = findLines();
        return persistLines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public List<Line> findLines() {
        return lineDao.findAll();
    }

    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        return LineResponse.of(persistLine);
    }

    public Line findLineById(Long id) {
        return lineDao.findById(id);
    }

    @Transactional
    public void updateLine(Long id, LineRequest request) {
        lineDao.updateById(id, Line.of(request.getName(), request.getColor()));
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineDao.deleteById(id);
    }

}
