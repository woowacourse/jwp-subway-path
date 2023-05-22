package subway.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.repository.LineRepository;
import subway.exception.IllegalLineException;
import subway.ui.dto.line.LineCreateRequest;
import subway.ui.dto.line.LineUpdateRequest;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public Line saveLine(LineCreateRequest lineCreateRequest) {
        Line line = new Line(lineCreateRequest.getName(), lineCreateRequest.getColor());
        if (lineRepository.isDuplicateLine(line)) {
            throw new IllegalLineException("해당 노선의 색, 또는 이름이 중복됩니다.");
        }
        long savedId = lineRepository.save(line);
        return new Line(savedId, line.getName(), line.getColor());
    }

    @Transactional(readOnly = true)
    public List<Line> findLines() {
        return lineRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Line findLineById(Long id) {
        return lineRepository.findById(id);
    }

    public Line updateLine(LineUpdateRequest lineUpdateRequest, long id) {
        return lineRepository.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(long id) {
        Line line = lineRepository.findByIdWithNoSections(id);
        lineRepository.delete(line);
    }
}
