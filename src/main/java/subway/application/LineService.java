package subway.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.entity.LineEntity;
import subway.domain.Line;
import subway.dto.line.LineCreateRequest;
import subway.dto.line.LineResponse;
import subway.dto.line.LineUpdateRequest;

@Service
public class LineService {
    private final LineDao lineDao;

    public LineService(LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public long saveLine(LineCreateRequest request) {
        return lineDao.insert(new Line(request.getLineName(), request.getColor()));
    }

    public List<LineResponse> findLineResponses() {
        List<LineEntity> lines = lineDao.findAll();
        return lines.stream()
                .map(LineResponse::from)
                .collect(toList());
    }

    public LineResponse findLineResponseById(Long id) {
        LineEntity lineEntity = lineDao.findById(id);
        return new LineResponse(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor());
    }

    public void updateLine(Long id, LineUpdateRequest lineUpdateRequest) {
        lineDao.update(id, new Line(lineUpdateRequest.getLineName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineDao.deleteById(id);
    }
}
