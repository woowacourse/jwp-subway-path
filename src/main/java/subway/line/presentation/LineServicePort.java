package subway.line.presentation;

import org.springframework.stereotype.Component;
import subway.line.Line;
import subway.line.application.LineService;
import subway.line.application.dto.LineUpdatingInfo;
import subway.line.domain.section.application.ShortestPathResponse;
import subway.line.domain.section.domain.Distance;
import subway.line.domain.section.dto.SectionSavingRequest;
import subway.line.domain.station.Station;
import subway.line.domain.station.application.StationService;
import subway.line.presentation.dto.LineRequest;
import subway.line.presentation.dto.LineResponse;
import subway.line.application.dto.LineSavingInfo;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LineServicePort {
    private final LineService lineService;
    private final StationService stationService;

    public LineServicePort(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public LineResponse saveLine(LineRequest lineRequest) {
        final var lineSavingInfo = new LineSavingInfo(lineRequest.getName(), lineRequest.getColor());
        final var savedLine = lineService.saveLine(lineSavingInfo);
        return convertLineToLineResponse(savedLine);
    }

    public List<LineResponse> findLineResponses() {
        final var lines = lineService.findLines();
        return lines.stream()
                .map(LineServicePort::convertLineToLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLineResponseById(Long id) {
        final var line = lineService.findLineById(id);
        return convertLineToLineResponse(line);
    }

    private static LineResponse convertLineToLineResponse(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.findAllStationsOrderByUp()
        );
    }

    public void updateLine(Long lineId, LineRequest lineUpdateRequest) {
        lineService.updateLine(new LineUpdatingInfo(lineId, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long lineId) {
        lineService.deleteLineById(lineId);
    }

    public long saveSection(long lineId, SectionSavingRequest sectionSavingRequest) {
        final var line = lineService.findLineById(lineId);
        final var previousStation = stationService.findById(sectionSavingRequest.getPreviousStationId());
        final var nextStation = stationService.findById(sectionSavingRequest.getNextStationId());

        if (sectionSavingRequest.isDown()) {
            return lineService.saveSection(line, previousStation, nextStation, sectionSavingRequest.getDistance());
        }
        return lineService.saveSection(line, nextStation, previousStation, sectionSavingRequest.getDistance());
    }

    public void deleteStation(Long lineId, Long stationId) {
        final var line = lineService.findLineById(lineId);
        final var station = stationService.findById(stationId);

        lineService.deleteStation(line, station);
    }

    public ShortestPathResponse findShortestPath(Long startingStationId, Long destinationStationId) {
        final var startingStation = stationService.findById(startingStationId);
        final var destinationStation = stationService.findById(destinationStationId);
        final var shortestPath = lineService.findShortestPath(startingStation, destinationStation);
        final var shortestDistance = lineService.findShortestDistance(startingStation, destinationStation);
        return new ShortestPathResponse(startingStation, destinationStation, shortestPath, shortestDistance.getValue());
    }

    public BigDecimal calculateFare(double distance) {
        return lineService.calculateFare(distance);
    }
}
