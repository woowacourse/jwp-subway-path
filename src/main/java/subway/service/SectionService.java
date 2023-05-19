package subway.service;

import org.springframework.stereotype.Service;
import subway.domain.*;
import subway.dto.request.CreateSectionRequest;
import subway.dto.request.RouteRequest;
import subway.dto.response.LineResponse;
import subway.dto.response.RouteResponse;
import subway.dto.response.StationResponse;
import subway.mapper.LineMapper;
import subway.mapper.StationMapper;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SectionService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public SectionService(final LineRepository lineRepository,
                          final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse createSection(final long lineId, final CreateSectionRequest request) {
        Line line = lineRepository.findById(lineId);

        Station upStation = stationRepository.findById(request.getUpStation());
        Station downStation = stationRepository.findById(request.getDownStation());

        line.addSection(new Section(upStation, downStation, request.getDistance()));

        lineRepository.updateSections(line);

        return LineMapper.toResponse(line);
    }

    public void deleteSection(final long lineId, final long stationId) {
        Line line = lineRepository.findById(lineId);

        Station station = stationRepository.findById(stationId);

        line.removeStation(station);

        lineRepository.updateSections(line);
    }

    public RouteResponse getShortestRoute(final RouteRequest routeRequest) {
        Station sourceStation = stationRepository.findById(routeRequest.getSourceStation());
        Station targetStation = stationRepository.findById(routeRequest.getTargetStation());

        ShortestRouteFinder finder = new ShortestRouteFinder(lineRepository.findAll());

        int distance = finder.getDistance(sourceStation, targetStation);
        long money = FeeCalculator.calculate(distance);
        List<StationResponse> route = finder.getRoute(sourceStation, targetStation).stream()
                .map(StationMapper::toResponse)
                .collect(Collectors.toList());

        return new RouteResponse(money, distance, route);
    }
}
