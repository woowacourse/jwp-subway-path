package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.Line;
import subway.domain.repository.LineRepository;
import subway.ui.dto.LineRequest;
import subway.ui.dto.LineResponse;

import java.util.List;

@Service
public class LineService {
    private final LineRepository lineRepository;

    public LineService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse createLine(final LineRequest lineRequest) {
        final Line line = new Line(lineRequest.getName());
        final Line createLine = lineRepository.createLine(line);

        return new LineResponse(createLine);
    }

    public void deleteLine(final Long lineIdRequest) {
        lineRepository.deleteById(lineIdRequest);
    }

    public List<LineResponse> findAll(){
        return LineResponse.of(lineRepository.findAll());
    }
}
