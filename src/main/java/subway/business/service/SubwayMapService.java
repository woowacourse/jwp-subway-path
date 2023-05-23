package subway.business.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.business.domain.line.Line;
import subway.business.domain.line.LineRepository;
import subway.business.domain.line.Station;
import subway.business.domain.line.Stations;
import subway.business.domain.subwaymap.JgraphtSubwayMap;
import subway.business.domain.subwaymap.Money;
import subway.business.domain.subwaymap.SubwayMap;
import subway.business.domain.transfer.TransferRepository;
import subway.business.service.dto.LinePathDto;
import subway.business.service.dto.SubwayPathResponse;


@Service
public class SubwayMapService {
    private final LineRepository lineRepository;
    private final TransferRepository transferRepository;

    public SubwayMapService(LineRepository lineRepository, TransferRepository transferRepository) {
        this.lineRepository = lineRepository;
        this.transferRepository = transferRepository;
    }

    @Transactional(readOnly = true)
    public SubwayPathResponse findPath(long sourceStationId, long targetStationId) {
        Station sourceStation = lineRepository.findStationById(sourceStationId);
        Station targetStation = lineRepository.findStationById(targetStationId);
        SubwayMap subwayMap = JgraphtSubwayMap.of(lineRepository.findAll(), transferRepository.findAll());
        Money fare = subwayMap.calculateFareOfPath(sourceStation, targetStation);
        List<Stations> stationsList = subwayMap.calculateShortestPath(sourceStation, targetStation);
        return mapPathResultToResponse(fare, stationsList);
    }

    private SubwayPathResponse mapPathResultToResponse(Money fare, List<Stations> stationsList) {
        List<LinePathDto> linePathDtosFromStationsList = stationsList.stream()
                .map(this::mapStationsToLinePathDto)
                .collect(Collectors.toList());
        return new SubwayPathResponse(fare, linePathDtosFromStationsList);
    }

    private LinePathDto mapStationsToLinePathDto(Stations stations) {
        Long firstStationsId = stations.getStations().get(0).getId();
        Line line = lineRepository.findLineByStationId(firstStationsId);
        return LinePathDto.of(line, stations);
    }
}
