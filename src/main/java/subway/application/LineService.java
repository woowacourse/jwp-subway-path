package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.domain.Direction;
import subway.domain.Line;
import subway.dto.LineRequest;
import subway.dto.LineResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {
    private final LineDao lineDao;

    public LineService(final LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public LineResponse saveLine(final LineRequest request) {
        final Line persistLine = lineDao.insert(new Line(request.getName(), request.getColor()));
        return LineResponse.of(persistLine);
    }

    public List<LineResponse> findLineResponses() {
        final List<Line> persistLines = findLines();
        return persistLines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public List<Line> findLines() {
        return lineDao.findAll();
    }

    public LineResponse findLineResponseById(final Long id) {
        final Line persistLine = findLineById(id);
        return LineResponse.of(persistLine);
    }

    public Line findLineById(final Long id) {
        return lineDao.findById(id);
    }

    public Line findLineByName(final String name) {
        return lineDao.findByName(name);
    }

    public void updateLine(final Long id, final LineRequest lineUpdateRequest) {
        lineDao.update(id, new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor(), null, null));
    }

    public void deleteLineById(final Long id) {
        lineDao.deleteById(id);
    }

    public void updateEndpoint(final Long lineId, final Direction direction, final Long stationId) {
        if (direction.equals(Direction.UP)) {
            lineDao.updateUpEndpoint(lineId, stationId);
            return;
        }
        lineDao.updateDownEndpoint(lineId, stationId);
    }
}
