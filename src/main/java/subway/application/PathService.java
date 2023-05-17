package subway.application;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional(readOnly = true)
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

        final Map<Long, Station> idToStationName = stations.stream()
                .collect(Collectors.toMap(Station::getId, Function.identity()));
        final Map<Long, Line> idToLineName = lines.stream()
                .collect(Collectors.toMap(Line::getId, Function.identity()));
        final List<SectionResponse> sectionResponses = path.stream()
                .map(section -> SectionResponse.from(
                                idToLineName.get(section.getLineId()),
                                idToStationName.get(section.getSourceStationId()),
                                idToStationName.get(section.getTargetStationId())
                        )
                )
                .collect(Collectors.toUnmodifiableList());
        return new PathResponse(distance, fare, sectionResponses);
    }
}
