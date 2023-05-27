package subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.dto.line.LineRequest;
import subway.dto.line.LineResponse;
import subway.repository.LineRepository;

@Transactional
@Service
public class LineService {

    private final LineRepository lineRepository;

    public LineService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public Long saveLine(final LineRequest request) {
        final Line line = new Line(request.getLineNumber(), request.getName(), request.getColor());
        return lineRepository.save(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAll() {
        return lineRepository.findAll().stream()
                .map(line -> {
                    final Long id = lineRepository.findLineIdByLine(line);
                    return new LineResponse(id, line.getLineNumber(), line.getName(), line.getColor());
                })
                .collect(Collectors.toList());
    }

    public void removeLineById(final Long id) {
        lineRepository.deleteByLineId(id);
    }
}
