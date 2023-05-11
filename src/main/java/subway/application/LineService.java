package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.LineDaoUnused;
import subway.domain.Line3;
import subway.dto.LineCreateDto;
import subway.dto.LineRequest;
import subway.dto.LineResponse3;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {
    private final LineDaoUnused lineDaoUnused;

    public LineService(LineDaoUnused lineDaoUnused) {
        this.lineDaoUnused = lineDaoUnused;
    }

    public LineResponse3 saveLine(LineCreateDto request) {
        return null;
    }

    public List<LineResponse3> findLineResponses() {
        List<Line3> persistLines = findLines();
        return persistLines.stream()
                .map(LineResponse3::of)
                .collect(Collectors.toList());
    }

    public List<Line3> findLines() {
        return lineDaoUnused.findAll();
    }

    public LineResponse3 findLineResponseById(Long id) {
        Line3 persistLine = findLineById(id);
        return LineResponse3.of(persistLine);
    }

    public Line3 findLineById(Long id) {
        return lineDaoUnused.findById(id);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        lineDaoUnused.update(new Line3(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineDaoUnused.deleteById(id);
    }
}
