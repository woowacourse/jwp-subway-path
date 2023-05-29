package subway.application;

import org.springframework.stereotype.Service;
import subway.application.repository.LineRepository;
import subway.domain.Line;
import subway.dto.LineRequest;
import subway.dto.LineResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final LineRepository lineRepository;

    public LineService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        final Line inserted = lineRepository.saveLine(new Line(request.getName(), request.getColor()));
        return LineResponse.of(inserted);
    }

    public LineResponse findLineResponseById(Long id) {
        Line line = lineRepository.findLineById(id);
        return LineResponse.of(line);
    }

    public List<LineResponse> findLineResponses() {
        List<Line> lines = lineRepository.findAllLines();
        return lines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteLineById(id);
    }

}
