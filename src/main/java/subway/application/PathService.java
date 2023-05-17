package subway.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.farecalculator.FareCalculator;
import subway.domain.pathfinder.PathFinder;
import subway.dto.PathResponse;
import subway.dto.SectionResponse;

@Service
public class PathService {
    private final PathFinder pathFinder;
    private final FareCalculator fareCalculator;
    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public PathService(
            PathFinder pathFinder,
            FareCalculator fareCalculator,
            LineDao lineDao,
            StationDao stationDao,
            SectionDao sectionDao
    ) {
        this.pathFinder = pathFinder;
        this.fareCalculator = fareCalculator;
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public PathResponse computePath(Long sourceStationId, Long targetStationId) {
        final List<Station> stations = stationDao.findAll();
        final List<Line> lines = lineDao.findAll();
        final List<Section> sections = sectionDao.findAll();
        

        pathFinder.addSections(sections);
        final int distance = pathFinder.computeShortestDistance(sourceStationId, targetStationId);
        final int fare = fareCalculator.calculateFare(distance);
        final List<Section> path = pathFinder.computeShortestPath(sourceStationId, targetStationId);

        final Map<Long, String> idToStationName = stations.stream()
                .collect(Collectors.toMap(Station::getId, Station::getName));
        final Map<Long, String> idToLineName = lines.stream()
                .collect(Collectors.toMap(Line::getId, Line::getName));
        final List<SectionResponse> sectionResponses = path.stream()
                .map(section -> new SectionResponse(
                        section.getLineId(),
                        idToLineName.get(section.getLineId()),
                        section.getSourceStationId(),
                        idToStationName.get(section.getSourceStationId()),
                        section.getTargetStationId(),
                        idToStationName.get(section.getTargetStationId())
                ))
                .collect(Collectors.toUnmodifiableList());
        return new PathResponse(distance,fare, sectionResponses);
    } 
}
