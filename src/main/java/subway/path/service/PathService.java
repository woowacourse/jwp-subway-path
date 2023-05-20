package subway.path.service;

import org.springframework.stereotype.Service;
import subway.path.CostCalculatePolicy;
import subway.path.CostCalculator;
import subway.path.domain.Path;
import subway.path.presentation.dto.response.PathResponse;
import subway.section.domain.Section;
import subway.section.domain.repository.SectionRepository;
import subway.station.domain.Station;
import subway.station.domain.repository.StationRepository;
import subway.station.presentation.dto.response.StationResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PathService {

    private final CostCalculatePolicy costCalculatePolicy;
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public PathService(final CostCalculator costCalculatePolicy, final SectionRepository sectionRepository, final StationRepository stationRepository) {
        this.costCalculatePolicy = costCalculatePolicy;
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findByDijkstra(final Long upStationId, final Long downStationId) {
        Station upStation = stationRepository.findById(upStationId);
        Station downStation = stationRepository.findById(downStationId);
        Path path = getPath();

        List<String> stationNames = path.findPath(upStation.getNameValue(), downStation.getNameValue());
        double pathDistance = path.findPathDistance(upStation.getNameValue(), downStation.getNameValue());
        int cost = costCalculatePolicy.calculateAdult((int) pathDistance);

        return new PathResponse(makeStationResponses(stationNames), pathDistance, cost);
    }

    private Path getPath() {
        List<Station> stations = stationRepository.findAll();
        List<Section> sections = sectionRepository.findAll();
        return new Path(stations, sections);
    }

    private List<StationResponse> makeStationResponses(final List<String> stationNames) {
        Map<String, Long> stations = stationRepository.findAll().stream()
                .collect(Collectors.toMap(Station::getNameValue, Station::getId));

        return stationNames.stream()
                .map(it -> new StationResponse(stations.get(it), it))
                .collect(Collectors.toList());
    }

}
