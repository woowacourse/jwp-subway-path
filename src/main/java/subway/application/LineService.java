package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.SectionRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {
    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(final LineRepository lineRepository, final StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public LineResponse saveLine(final LineRequest request) {
        final Line persistLine = lineRepository.saveLine(request.getName(), request.getColor());
        return LineResponse.of(persistLine);
    }

    public LineResponse saveInitialSection(final Long lineId, final SectionRequest sectionRequest) {
        final Line line = lineRepository.findLineById(lineId);
        final Station upStation = stationService.findStationById(sectionRequest.getUpStationId());
        final Station downStation = stationService.findStationById(sectionRequest.getDownStationId());
        final int distance = sectionRequest.getDistance();

        line.addInitialSection(upStation, downStation, distance);
        lineRepository.saveSectionsByLine(line);

        return LineResponse.of(line);
    }

    public LineResponse saveSection(final Long lineId, final SectionRequest sectionRequest) {
        final Line line = lineRepository.findLineById(lineId);
        final Station upStation = stationService.findStationById(sectionRequest.getUpStationId());
        final Station downStation = stationService.findStationById(sectionRequest.getDownStationId());
        final int distance = sectionRequest.getDistance();

        line.addSection(upStation, downStation, distance);
        lineRepository.saveSectionsByLine(line);

        return LineResponse.of(line);
    }

    public List<LineResponse> findLineResponses() {
        final List<Line> persistLines = lineRepository.findLines();
        return persistLines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public LineResponse findLineResponseById(final Long id) {
        final Line persistLine = lineRepository.findLineById(id);
        return LineResponse.of(persistLine);
    }

    public void updateLine(final Long id, final LineRequest lineUpdateRequest) {
        lineRepository.updateLine(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor());
    }

    public void deleteStationById(final Long lineId, final Long stationId) {
        final Line line = lineRepository.findLineById(lineId);
        final Station station = stationService.findStationById(stationId);

        line.deleteStation(station);
        lineRepository.saveSectionsByLine(line);
    }

    public void deleteLineById(final Long id) {
        lineRepository.deleteLineById(id);
    }
}
