package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.path.Fare;
import subway.domain.path.Path;
import subway.domain.station.Station;
import subway.domain.station.StationName;
import subway.persistence.repository.PathRepository;
import subway.ui.request.PathRequest;
import subway.ui.response.PathResponse;

import java.util.List;

@Service
public class PathService {

    private final PathRepository serviceRepository;

    public PathService(final PathRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public PathResponse findPath(final PathRequest pathRequest) {
        final StationName startSection = new StationName(pathRequest.getStartStationName());
        final StationName endSection = new StationName(pathRequest.getEndStationName());
        final Path path = serviceRepository.findAll();
        final List<Station> shortestPathStations = path.findShortestPath(startSection, endSection);
        final double distance = path.findShortestDistance(startSection, endSection);
        final int fare = Fare.calculateFare(distance);
        return new PathResponse(fare, distance, shortestPathStations);
    }
}
