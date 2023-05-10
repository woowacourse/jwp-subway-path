package subway.service;

import org.springframework.stereotype.Service;
import subway.controller.dto.LineCreateRequest;
import subway.controller.dto.LineResponse;
import subway.controller.dto.LinesResponse;
import subway.controller.dto.SectionCreateRequest;
import subway.domain.line.Line;
import subway.repository.LineRepository;

@Service
public class LineService {

    private final LineRepository lineRepository;

    public LineService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public Long createLine(final LineCreateRequest request) {
        final Line line = new Line(request.getName(), request.getColor());
        return lineRepository.save(line).getId();
    }

    public LineResponse findLineById(final Long lineId) {
        final Line line = lineRepository.findById(lineId);
        return LineResponse.from(line);
    }

    public LinesResponse findLines() {
        return null;
    }

    public void createSection(final SectionCreateRequest request) {

    }

    public void deleteStation(final Long lineId, final Long stationId) {

    }
}
