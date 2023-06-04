package subway.service.line;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.line.Line;
import subway.domain.line.LineRepository;
import subway.dto.line.LineCreateRequest;
import subway.dto.line.LineResponse;

@Transactional
@Service
public class LineService {

    private final LineRepository lineRepository;

    public LineService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public Long save(final LineCreateRequest request) {
        final Line line = new Line(request.getLineNumber(), request.getName(), request.getColor(), request.getAdditionalFare());
        return lineRepository.save(line);
    }

    public void removeById(final Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAll() {
        return lineRepository.findAll().stream()
                .map(line -> new LineResponse(
                        line.getId(),
                        line.getLineNumber(),
                        line.getName(),
                        line.getColor(),
                        line.getAdditionalFare()))
                .collect(Collectors.toList());
    }
}
