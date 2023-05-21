package subway.application.line;

import static java.util.stream.Collectors.toList;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.entity.LineEntity;
import subway.domain.line.Line;
import subway.domain.line.LineColor;
import subway.domain.line.LineName;
import subway.dto.line.LineCreateRequest;
import subway.dto.line.LineResponse;
import subway.dto.line.LineUpdateRequest;
import subway.exception.line.DuplicateLineException;
import subway.exception.line.LineNotFoundException;

@Service
@Transactional
public class LineService {
    private final LineDao lineDao;

    public LineService(LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public long saveLine(LineCreateRequest request) {
        LineName lineName = new LineName(request.getLineName());
        LineColor lineColor = new LineColor(request.getColor());
        if (lineDao.existsByName(request.getLineName())) {
            throw new DuplicateLineException();
        }
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
        LineEntity lineEntity = lineDao.findById(id)
                .orElseThrow(LineNotFoundException::new);
        return new LineResponse(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor());
    }

    public void updateLine(Long id, LineUpdateRequest request) {
        LineName lineName = new LineName(request.getLineName());
        LineColor lineColor = new LineColor(request.getColor());
        if (lineDao.doesNotExistById(id)) {
            throw new LineNotFoundException();
        }
        lineDao.update(id, new Line(lineName, lineColor));
    }

    public void deleteLineById(Long id) {
        if (lineDao.doesNotExistById(id)) {
            throw new LineNotFoundException();
        }
        lineDao.deleteById(id);
    }
}
