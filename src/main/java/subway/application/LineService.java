package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.domain.line.Line;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.repository.LineRepository;

@Service
public class LineService {
    private final LineRepository lineRepository;

    public LineService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        Line persistLine = lineRepository.save(new Line(request.getName(), request.getColor()));
        return LineResponse.of(persistLine);
    }
}
