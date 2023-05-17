package subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.domain.Distance;
import subway.domain.Fare;
import subway.domain.FarePolicy;
import subway.domain.Station;
import subway.domain.path.ShortestPathFinder;
import subway.domain.section.EmptySections;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;
import subway.service.dto.PathRequest;
import subway.service.dto.PathResponse;
import subway.service.dto.StationResponse;

@Service
public class PathService {

    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final ShortestPathFinder finder;
    private final FarePolicy farePolicy;

    public PathService(final SectionDao sectionDao, final StationDao stationDao, final ShortestPathFinder finder,
                       final FarePolicy farePolicy) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.finder = finder;
        this.farePolicy = farePolicy;
    }

    public PathResponse findPath(final PathRequest pathRequest) {
        final List<Section> allSections = sectionDao.findAll();
        final Station startStation = stationDao.findByName(pathRequest.getStartStationName())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));
        final Station endStation = stationDao.findByName(pathRequest.getEndStationName())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));
        final List<Section> path = finder.findShortestPath(allSections, startStation, endStation);
        final Sections sections = createSections(path);
        return toPathResponse(sections);
    }

    private Sections createSections(final List<Section> path) {
        Sections sections = new EmptySections();
        for (final Section section : path) {
            sections = sections.addSection(section);
        }
        return sections;
    }

    private PathResponse toPathResponse(final Sections sections) {
        final List<StationResponse> stationResponses = sections.getAllStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toUnmodifiableList());
        final Distance totalDistance = sections.getSections().stream()
                .map(Section::getDistance)
                .reduce(Distance::plusValue)
                .orElseThrow(() -> new IllegalArgumentException("존재하는 경로가 없습니다."));
        final Fare fare = farePolicy.calculate(sections.getSections());
        return new PathResponse(stationResponses, fare.getValue(), totalDistance.getValue());
    }
}
