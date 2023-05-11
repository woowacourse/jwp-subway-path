package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.domain.line.Line;
import subway.domain.line.Lines;
import subway.dto.LineRequest;
import subway.dto.LineResponse;

@Service
public class LineService {
    private final LineDao lineDao;

    public LineService(LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public LineResponse saveLine(final LineRequest request) {
        final List<Line> lineEntities = lineDao.findAll();
        final Lines lines = convertLines(lineEntities);
        final Line requestLine = new Line(request.getName(), request.getColor());
        lines.validateDuplication(requestLine);
        final Line savedLine = lineDao.insert(requestLine);
        return LineResponse.of(savedLine.getId(), savedLine.getName(), savedLine.getColor());
    }

    public void updateLine(final Long id, final LineRequest lineUpdateRequest) {
        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineDao.deleteById(id);
    }

    private Lines convertLines(final List<Line> lineEntities) {
        final List<Line> lines = lineEntities.stream()
            .map(Line -> new Line(Line.getName(), Line.getName()))
            .collect(Collectors.toUnmodifiableList());
        return new Lines(lines);
    }
}
