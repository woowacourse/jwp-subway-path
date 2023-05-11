package subway.application;

import java.util.Collections;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.dto.LineResponse;
import subway.dto.LineSaveRequest;
import subway.dto.LineUpdateRequest;
import subway.repository.LineRepository;

@Transactional
@Service
public class LineService {

    private final LineRepository lineRepository;

    public LineService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public Long save(final LineSaveRequest request) {
        final Line line = new Line(request.getName(), request.getColor(), Collections.emptyList());
        lineRepository.save(line);
        return lineRepository.findIdByName(request.getName());
    }

    public void delete(final Long id) {
        lineRepository.deleteById(id);
    }

    public void update(final Long id, final LineUpdateRequest request) {
        lineRepository.updateNameAndColorById(id, request.getName(), request.getColor());
    }

    public LineResponse findById(final Long id) {
        final Line line = lineRepository.findById(id);
        return LineResponse.from(line);
    }
}
