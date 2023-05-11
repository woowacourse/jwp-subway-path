package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.LineDao2;
import subway.domain.Line3;
import subway.dto.LineCreateDto;
import subway.dto.LineRequest;
import subway.dto.LineResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {
    private final LineDao2 lineDao2;

    public LineService(LineDao2 lineDao2) {
        this.lineDao2 = lineDao2;
    }

    public LineResponse saveLine(LineCreateDto request) {
        return null;
    }

    public List<LineResponse> findLineResponses() {
        List<Line3> persistLines = findLines();
        return persistLines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public List<Line3> findLines() {
        return lineDao2.findAll();
    }

    public LineResponse findLineResponseById(Long id) {
        Line3 persistLine = findLineById(id);
        return LineResponse.of(persistLine);
    }

    public Line3 findLineById(Long id) {
        return lineDao2.findById(id);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        lineDao2.update(new Line3(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineDao2.deleteById(id);
    }
}
