package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Lines;
import subway.dto.request.LineRequest;
import subway.dto.response.LineResponse;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class LineService {

    private final LineRepository lineRepository;

    public LineService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse createLine(final LineRequest request) {
        final Line line = request.toLine();
        final Line savedLine = lineRepository.save(line);

        return LineResponse.of(savedLine);
    }

    public void save(final Line line) {
        lineRepository.save(line);
    }

    public void updateLine(final Long id, final LineRequest lineUpdateRequest) {
        final Line line = findById(id);

        line.update(lineUpdateRequest.getName(), lineUpdateRequest.getColor());

        lineRepository.save(line);
    }

    public void deleteLineById(final Long id) {
        lineRepository.findById(id).ifPresent(lineRepository::delete);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findLineResponses() {
        return lineRepository.findAll().getLines().stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public LineResponse findLineResponseById(final Long id) {
        return LineResponse.of(findById(id));
    }

    @Transactional(readOnly = true)
    public Line findById(final Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id에 해당하는 라인이 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public Lines findAll() {
        return lineRepository.findAll();
    }
}
