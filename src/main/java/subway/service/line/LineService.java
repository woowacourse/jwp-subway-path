package subway.service.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.persistence.repository.LineRepository;

import java.util.List;

@Transactional
@Service
public class LineService {
    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        return lineRepository.saveLine(request);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findLineResponses();
    }

    public LineResponse findLineById(Long id) {
        return lineRepository.findLineResponseById(id);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        lineRepository.updateLine(id, lineUpdateRequest);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteLineById(id);
    }
}
