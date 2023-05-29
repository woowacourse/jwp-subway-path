package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.line.Line;
import subway.dto.request.LineRequest;
import subway.dto.response.LineResponse;
import subway.repository.LineRepository;

@Transactional
@Service
public class LineService {
    private final LineRepository lineRepository;

    public LineService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        Line line = lineRepository.insert(new Line(request.getName(), request.getColor()));
        return LineResponse.from(line);
    }

    public List<LineResponse> findLineResponses() {
        List<Line> lines = findAll();
        return lines.stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }

    private List<Line> findAll() {
        return lineRepository.findAll();
    }

    public LineResponse findLineResponseById(Long id) {
        Line line = findLineById(id);
        return LineResponse.from(line);
    }

    public Line findLineById(Long id) {
        return lineRepository.findById(id);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        lineRepository.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }
}
