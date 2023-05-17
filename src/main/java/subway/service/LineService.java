package subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.dto.request.LineCreateRequest;
import subway.controller.dto.request.SectionCreateRequest;
import subway.controller.dto.response.LineResponse;
import subway.controller.dto.response.LinesResponse;
import subway.domain.line.Line;
import subway.domain.station.Station;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(final LineRepository lineRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public Long createLine(final LineCreateRequest request) {
        final Line line = new Line(request.getName(), request.getColor(), request.getFare());
        return lineRepository.save(line).getId();
    }

    public LineResponse findLineById(final Long lineId) {
        final Line line = lineRepository.findById(lineId);
        return LineResponse.from(line);
    }

    public LinesResponse findLines() {
        final List<Line> lines = lineRepository.findAll();
        return new LinesResponse(generateLineResponses(lines));
    }

    private List<LineResponse> generateLineResponses(final List<Line> lines) {
        return lines.stream()
                .map(LineResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public void createSection(final Long lineId, final SectionCreateRequest request) {
        final Line line = lineRepository.findById(lineId);
        final Station upward = stationRepository.findById(request.getUpwardStationId());
        final Station downward = stationRepository.findById(request.getDownwardStationId());
        line.addSection(upward, downward, request.getDistance());
        lineRepository.update(line);
    }

    @Transactional
    public void deleteStation(final Long lineId, final Long stationId) {
        final Line line = lineRepository.findById(lineId);
        final Station station = stationRepository.findById(stationId);
        line.deleteStation(station);
        lineRepository.update(line);
    }
}
