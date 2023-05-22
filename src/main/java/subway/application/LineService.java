package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.dto.request.LineRequest;
import subway.controller.dto.response.LineResponse;
import subway.domain.Line;
import subway.exception.BusinessException;
import subway.persistence.LineRepository;

@Transactional(readOnly = true)
@Service
public class LineService {

    private final LineRepository lineRepository;

    public LineService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional
    public LineResponse saveLine(final LineRequest request) {
        final Line persistLine = lineRepository.save(new Line(request.getName(), request.getColor(), 0L));
        return LineResponse.of(persistLine);
    }

    public List<LineResponse> findLineResponses() {
        final List<Line> persistLines = findLines();
        return persistLines.stream()
            .map(LineResponse::of)
            .collect(Collectors.toList());
    }

    public List<Line> findLines() {
        return lineRepository.findAll();
    }

    public LineResponse findLineResponseById(final Long id) {
        final Line persistLine = findLineById(id);
        return LineResponse.of(persistLine);
    }

    public Line findLineById(final Long id) {
        return lineRepository.findById(id)
            .orElseThrow(() -> new BusinessException("존재하지 않는 호선입니다."));
    }

    @Transactional
    public void updateLine(final Long id, final LineRequest lineUpdateRequest) {
        lineRepository.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor(), 0L));
    }

    @Transactional
    public void deleteLineById(final Long id) {
        lineRepository.deleteById(id);
    }
}
