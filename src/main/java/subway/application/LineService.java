package subway.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.dto.LineCreateDto;
import subway.application.dto.LineUpdateDto;
import subway.domain.Line;
import subway.domain.repository.LineRepository;
import subway.exception.IllegalLineException;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public Line saveLine(LineCreateDto requestedLine) {
        Line line = new Line(requestedLine.getName(), requestedLine.getColor());
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

    public Line updateLine(LineUpdateDto requestedLine) {
        return lineRepository.update(new Line(requestedLine.getId(), requestedLine.getName(), requestedLine.getColor()));
    }

    public void deleteLineById(Long id) {
        Line line = lineRepository.findByIdWithNoSections(id);
        lineRepository.delete(line);
    }
}
