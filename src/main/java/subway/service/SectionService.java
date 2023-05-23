package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional
public class SectionService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final RouteFinder routeFinder;

    public SectionService(final LineRepository lineRepository,
                          final StationRepository stationRepository, RouteFinder routeFinder) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.routeFinder = routeFinder;
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

    @Transactional(readOnly = true)
    public RouteResponse getShortestRoute(final RouteRequest routeRequest) {
        Station sourceStation = stationRepository.findById(routeRequest.getSourceStation());
        Station targetStation = stationRepository.findById(routeRequest.getTargetStation());

        int distance = routeFinder.getDistance(sourceStation, targetStation);
        int surcharge = routeFinder.getSurcharge(sourceStation, targetStation);
        long fare = FareCalculator.calculate(distance, surcharge, routeRequest.getAge());
        List<StationResponse> route = routeFinder.findRoute(sourceStation, targetStation).stream()
                .map(StationMapper::toResponse)
                .collect(Collectors.toList());

        return new RouteResponse(fare, distance, route);
    }
}
