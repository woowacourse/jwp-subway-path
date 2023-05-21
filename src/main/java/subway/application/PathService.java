package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.fare.FareCalculator;
import subway.domain.path.Path;
import subway.domain.path.PathFinder;
import subway.domain.section.Section;
import subway.domain.section.SectionRepository;
import subway.domain.section.Sections;
import subway.dto.PathRequest;
import subway.dto.PathResponse;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final SectionRepository sectionRepository;
    private final FareCalculator fareCalculator;

    public PathService(SectionRepository sectionRepository, FareCalculator fareCalculator) {
        this.sectionRepository = sectionRepository;
        this.fareCalculator = fareCalculator;
    }

    public PathResponse findShortestPath(PathRequest pathRequest) {
        List<Section> allSections = sectionRepository.findAllSections();
        Path path = PathFinder.findPath(new Sections(allSections), pathRequest.getStartStationName(), pathRequest.getEndStationName());
        List<String> stationNames = path.getStationNames();
        int totalDistance = path.getDistance();
        int fare = fareCalculator.calculate(totalDistance);
        return new PathResponse(stationNames, totalDistance, fare);
    }
}
