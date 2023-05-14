package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.dto.request.LineRequest;
import subway.dto.response.LineResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse createLine(final LineRequest request) {
        Line line = new Line(request);
        Line savedLine = lineRepository.save(line);

        return LineResponse.of(savedLine);
    }

    public void save(Line line) {
        lineRepository.save(line);
    }

    public void updateLine(final Long id, final LineRequest lineUpdateRequest) {
        Line line = findById(id);

        line.update(lineUpdateRequest.getName(), lineUpdateRequest.getColor());

        lineRepository.save(line);
    }

    public void deleteLineById(final Long id) {
        lineRepository.findById(id).ifPresent(lineRepository::delete);
    }

    public List<LineResponse> findLineResponses() {
        final List<Line> persistLines = lineRepository.findAll();
        return persistLines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public LineResponse findLineResponseById(final Long id) {
        return LineResponse.of(findById(id));
    }

    public Line findById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id에 해당하는 라인이 존재하지 않습니다."));
    }

}
