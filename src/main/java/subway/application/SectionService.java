package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.section.SectionGraph;
import subway.domain.station.Station;
import subway.domain.station.StationName;
import subway.persistence.repository.SectionRepository;
import subway.ui.request.PathRequest;
import subway.ui.response.PathResponse;

import java.util.List;

@Service
public class SectionService {

    private final SectionRepository serviceRepository;

    public SectionService(final SectionRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public PathResponse findPath(final PathRequest pathRequest) {
        final StationName startSection = new StationName(pathRequest.getStartStationName());
        final StationName endSection = new StationName(pathRequest.getEndStationName());
        final SectionGraph sectionGraph = serviceRepository.findAll();
        final List<Station> shortestPathStations = sectionGraph.findShortestPath(startSection, endSection);
        final double distance = sectionGraph.findShortestDistance(startSection, endSection);
        final int fare = SectionGraph.calculateFare(distance);
        return new PathResponse(fare, distance, shortestPathStations);
    }
}
