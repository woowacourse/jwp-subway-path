package subway.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.fare.FarePolicy;
import subway.domain.pathfinder.ShortestPath;
import subway.domain.pathfinder.SubwayMapShortestPathFinder;
import subway.domain.subwaymap.Line;
import subway.domain.subwaymap.LineStation;
import subway.domain.subwaymap.Section;
import subway.domain.subwaymap.Sections;
import subway.domain.subwaymap.Station;
import subway.domain.subwaymap.SubwayMap;
import subway.dto.response.LineStationResponse;
import subway.dto.response.ShortestPathResponse;
import subway.exception.custom.LineNotExistException;
import subway.exception.custom.StationNotExistException;

@Service
public class ShortestPathService {

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final SubwayMapShortestPathFinder subwayMapShortestPathFinder;
    private final FarePolicy farePolicy;

    public ShortestPathService(final SubwayMapShortestPathFinder subwayMapShortestPathFinder,
        final FarePolicy farePolicy,
        final LineDao lineDao,
        final StationDao stationDao, final SectionDao sectionDao) {
        this.subwayMapShortestPathFinder = subwayMapShortestPathFinder;
        this.farePolicy = farePolicy;
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public ShortestPathResponse getPath(final String startStation, final String endStation, final int age) {
        final Station start = stationDao.findByName(startStation)
            .orElseThrow(() -> new StationNotExistException("해당 역이 지하철 노선도에 존재하지 않습니다. ( " + startStation + " )"));
        final Station end = stationDao.findByName(endStation)
            .orElseThrow(() -> new StationNotExistException("해당 역이 지하철 노선도에 존재하지 않습니다. ( " + endStation + " )"));

        final SubwayMap subwayMap = generateSubwayMap();
        final ShortestPath shortestPath = subwayMap.getShortestPath(start, end);

        final List<LineStation> path = new ArrayList<>(shortestPath.getPath());
        setFirstStationLine(subwayMap, path);

        final int totalDistance = shortestPath.getDistance();
        final int totalFare = farePolicy.calculate(0, path, totalDistance, age);

        return new ShortestPathResponse(mapToLineStationResponse(path), totalDistance, totalFare);
    }

    private SubwayMap generateSubwayMap() {
        final List<Line> lines = lineDao.findAll();
        final List<Sections> lineSections = lines.stream()
            .map(this::findSectionsInLine)
            .collect(Collectors.toList());

        return new SubwayMap(lines, lineSections, subwayMapShortestPathFinder);
    }

    private Sections findSectionsInLine(final Line line) {
        if (lineDao.findById(line.getId()).isEmpty()) {
            throw new LineNotExistException("역을 등록하려는 노선이 존재하지 않습니다.");
        }
        final List<Section> sectionsInLine = sectionDao.findAllByLineId(line.getId());
        return Sections.of(sectionsInLine);
    }

    private void setFirstStationLine(final SubwayMap subwayMap, final List<LineStation> path) {
        final LineStation firstStation = path.get(0);
        final Line firstStationLine = findFirstStationLine(subwayMap, path);

        path.set(0, LineStation.of(firstStationLine, firstStation.getStation()));
    }

    private Line findFirstStationLine(final SubwayMap subwayMap, final List<LineStation> shortestPath) {
        if (isPathToSelf(shortestPath)) {
            final Station firstStation = shortestPath.get(0).getStation();
            return subwayMap.findLineContains(firstStation);
        }

        return shortestPath.get(1).getLine();
    }

    private boolean isPathToSelf(final List<LineStation> path) {
        return path.size() == 1;
    }

    private List<LineStationResponse> mapToLineStationResponse(final List<LineStation> path) {
        return path
            .stream()
            .map(LineStationResponse::of)
            .collect(Collectors.toList());
    }
}
