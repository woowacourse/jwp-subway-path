package subway.service.line;

import org.springframework.stereotype.Service;
import subway.service.line.domain.Line;
import subway.service.line.dto.LineRequest;
import subway.service.line.dto.LineResponse;

@Service
public class LineService {
    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        Line persistLine = lineRepository.insert(new Line(request.getName(), request.getColor()));
        return LineResponse.of(persistLine);
    }

}
