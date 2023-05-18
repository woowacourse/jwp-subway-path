package subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.domain.Distance;
import subway.domain.Station;
import subway.domain.fare.Fare;
import subway.domain.fare.FarePolicy;
import subway.domain.path.Path;
import subway.domain.path.ShortestPathFinder;
import subway.domain.section.EmptySections;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.exception.StationNotFoundException;
import subway.persistence.dao.SectionDao;
import subway.persistence.repository.StationRepository;
import subway.service.dto.PathRequest;
import subway.service.dto.PathResponse;
import subway.service.dto.StationResponse;

@Service
public class PathService {

    private final SectionDao sectionDao;
    private final StationRepository stationRepository;
    private final ShortestPathFinder finder;
    private final FarePolicy farePolicy;

    public PathService(final SectionDao sectionDao, final StationRepository stationRepository,
                       final ShortestPathFinder finder,
                       final FarePolicy farePolicy) {
        this.sectionDao = sectionDao;
        this.stationRepository = stationRepository;
        this.finder = finder;
        this.farePolicy = farePolicy;
    }

    public PathResponse findPath(final PathRequest pathRequest) {
        final List<Section> allSections = sectionDao.findAll();
        final Station startStation = stationRepository.findByName(pathRequest.getStartStationName())
                .orElseThrow(StationNotFoundException::new);
        final Station endStation = stationRepository.findByName(pathRequest.getEndStationName())
                .orElseThrow(StationNotFoundException::new);
        final Path path = finder.findShortestPath(allSections, startStation, endStation);
        return toPathResponse(path);
    }

    private PathResponse toPathResponse(final Path path) {
        final List<StationResponse> stationResponses = path.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toUnmodifiableList());
        final Sections sections = createSections(path.getSections());
        final Distance distance = sections.getTotalDistance();
        final Fare fare = farePolicy.calculate(distance);
        return new PathResponse(stationResponses, fare.getValue(), distance.getValue());
    }

    private Sections createSections(final List<Section> sections) {
        Sections result = new EmptySections();
        for (final Section section : sections) {
            result = result.addSection(section);
        }
        return result;
    }
}
