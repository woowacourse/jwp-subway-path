package subway.application.line;

import org.springframework.stereotype.Service;
import subway.domain.Line;
import subway.domain.repository.LineRepository;
import subway.ui.dto.request.LineRequest;

import java.util.Optional;

@Service
public class CreateLineService {
    private final LineRepository lineRepository;

    public CreateLineService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public Long createLine(final LineRequest lineRequest) {
        final Line line = new Line(lineRequest.getName());

        Optional<Line> lineEntity = lineRepository.findByName(line);

        if (lineEntity.isEmpty()) {
            return lineRepository.createLine(line);
        }
        throw new IllegalArgumentException("이미 저장되어있는 노선입니다.");
    }
}
