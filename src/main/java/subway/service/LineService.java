package subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.LineName;
import subway.domain.Station;
import subway.domain.section.Section;
import subway.persistence.repository.LineRepository;
import subway.service.dto.LineRequest;
import subway.service.dto.LineResponse;
import subway.service.dto.SectionRequest;
import subway.service.dto.StationRequest;

@Service
public class LineService {

    private final StationService stationService;
    private final LineRepository lineRepository;

    public LineService(final StationService stationService, final LineRepository lineRepository) {
        this.stationService = stationService;
        this.lineRepository = lineRepository;
    }

    @Transactional
    public LineResponse saveLine(final LineRequest request) {
        final Line persistLine = lineRepository.insert(new LineName(request.getName()));
        return LineResponse.from(persistLine);
    }

    public List<LineResponse> findLineResponses() {
        final List<Line> persistLines = lineRepository.findAll();
        return persistLines.stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }

    public LineResponse findLineResponseById(final Long id) {
        final Line line = lineRepository.findById(id);
        return LineResponse.from(line);
    }

    @Transactional
    public void registerStation(final Long lineId, final SectionRequest request) {
        final Line line = lineRepository.findById(lineId);

        final Section section = createSections(request);
        final Line addedLine = line.addSection(section);

        lineRepository.updateLine(line, addedLine);
    }

    @Transactional
    public void unregisterStation(final Long lineId, final StationRequest request) {
        final Line line = lineRepository.findById(lineId);

        final Station station = stationService.findStationByName(request.getName());
        final Line deletedLine = line.removeStation(station);

        lineRepository.updateLine(line, deletedLine);
    }

    private Section createSections(final SectionRequest request) {
        final Station beforeStation = stationService.findStationByName(request.getPrevStationName());
        final Station nextStation = stationService.findStationByName(request.getNextStationName());
        return new Section(beforeStation, nextStation, new Distance(request.getDistance()));
    }

    @Transactional
    public void updateLine(final Long id, final LineRequest lineUpdateRequest) {
        lineRepository.updateName(id, new LineName(lineUpdateRequest.getName()));
    }

    @Transactional
    public void deleteLineById(final Long id) {
        lineRepository.deleteById(id);
    }
}
