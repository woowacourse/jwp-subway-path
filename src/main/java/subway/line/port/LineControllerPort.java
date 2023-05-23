package subway.line.port;

import org.springframework.stereotype.Component;
import subway.line.Line;
import subway.line.UnRegisteredLine;
import subway.line.application.LineService;
import subway.line.application.dto.CustomerConditionInfo;
import subway.line.application.dto.LineUpdatingInfo;
import subway.line.domain.fare.Fare;
import subway.line.domain.fare.application.domain.Age;
import subway.line.domain.section.application.ShortestPathResponse;
import subway.line.domain.station.application.StationService;
import subway.line.presentation.dto.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LineControllerPort {
    private final LineService lineService;
    private final StationService stationService;

    public LineControllerPort(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public LineResponse saveLine(LineRequest lineRequest) {
        final var unRegisteredLine = new UnRegisteredLine(lineRequest.getName(), lineRequest.getColor());
        final var savedLine = lineService.saveLine(unRegisteredLine);
        return convertLineToLineResponse(savedLine);
    }

    public List<LineResponse> findLineResponses() {
        final var lines = lineService.findLines();
        return lines.stream()
                .map(LineControllerPort::convertLineToLineResponse)
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
        final var line = lineService.findLineById(lineId);
        lineService.updateLine(line, new LineUpdatingInfo(lineUpdateRequest.getName(), lineUpdateRequest.getColor()));

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

    public BigDecimal calculateFare(SectionRequest sectionRequest) {
        final var startingStation = stationService.findById(sectionRequest.getStartingStationId());
        final var destinationStation = stationService.findById(sectionRequest.getDestinationStationId());
        final var customerConditionInfo = new CustomerConditionInfo(Age.of(sectionRequest.getAge()));

        final var fare = lineService.calculateFare(startingStation, destinationStation, customerConditionInfo);
        return fare.getMoney();
    }

    public long saveSurcharge(long lineId, SurchargeRequest surchargeRequest) {
        lineService.saveSurcharge(lineId, new Fare(new BigDecimal(surchargeRequest.getSurcharge())));
        return lineId;
    }
}
