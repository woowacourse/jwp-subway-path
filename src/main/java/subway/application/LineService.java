package subway.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.entity.LineEntity;
import subway.domain.Line;
import subway.domain.LineColor;
import subway.domain.LineName;
import subway.dto.line.LineCreateRequest;
import subway.dto.line.LineResponse;
import subway.dto.line.LineUpdateRequest;
import subway.exception.DuplicateLineException;

@Service
@Transactional
public class LineService {
    private final LineDao lineDao;

    public LineService(LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public long saveLine(LineCreateRequest request) {
        if (lineDao.existsByName(request.getLineName())) {
            throw new DuplicateLineException();
        }
        LineName lineName = new LineName(request.getLineName());
        LineColor lineColor = new LineColor(request.getColor());
        return lineDao.insert(new Line(lineName, lineColor));
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findLineResponses() {
        List<LineEntity> lines = lineDao.findAll();
        return lines.stream()
                .map(LineResponse::from)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLineResponseById(Long id) {
        LineEntity lineEntity = lineDao.findById(id);
        return new LineResponse(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor());
    }

    public void updateLine(Long id, LineUpdateRequest request) {
        LineName lineName = new LineName(request.getLineName());
        LineColor lineColor = new LineColor(request.getColor());
        lineDao.update(id, new Line(lineName, lineColor));
    }

    public void deleteLineById(Long id) {
        lineDao.deleteById(id);
    }
}
