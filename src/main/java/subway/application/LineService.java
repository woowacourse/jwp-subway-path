package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.SectionRequest;
import subway.dto.SectionResponse;

import java.util.ArrayList;
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

    public SectionResponse saveInitialSection(final Long lineId, final SectionRequest sectionRequest) {
        final Line line = lineRepository.findLineById(lineId);
        final Station upStation = stationService.findStationById(sectionRequest.getUpStationId());
        final Station downStation = stationService.findStationById(sectionRequest.getDownStationId());
        final int distance = sectionRequest.getDistance();

        final Section section = line.addInitialSection(upStation, downStation, distance);
        final Long sectionId = lineRepository.saveSection(line, section);
        lineRepository.updateUpEndpoint(line);

        return SectionResponse.of(sectionId, section);
    }

    public SectionResponse saveSection(final Long lineId, final SectionRequest sectionRequest) {
        final Line line = lineRepository.findLineById(lineId);
        final Station upStation = stationService.findStationById(sectionRequest.getUpStationId());
        final Station downStation = stationService.findStationById(sectionRequest.getDownStationId());
        final int distance = sectionRequest.getDistance();

        final Section section = line.addSection(upStation, downStation, distance);

        final List<Long> sectionIds = resaveSectionsInLine(line);

        final Long lastSectionId = sectionIds.get(sectionIds.size() - 1);
        return SectionResponse.of(lastSectionId, section);
    }

    private List<Long> resaveSectionsInLine(final Line line) {
        lineRepository.deleteSectionsByLine(line);

        final List<Long> sectionIds = new ArrayList<>();
        line.getSections().forEach(s ->
                sectionIds.add(lineRepository.saveSection(line, s))
        );

        lineRepository.updateUpEndpoint(line);

        return sectionIds;
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
        resaveSectionsInLine(line);
    }

    public void deleteLineById(final Long id) {
        lineRepository.deleteLineById(id);
    }
}
