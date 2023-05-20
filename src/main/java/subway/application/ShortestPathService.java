package subway.application;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.dao.SubwayMapRepository;
import subway.domain.FareCalculator;
import subway.domain.Line;
import subway.domain.LineStation;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.ShortestPath;
import subway.domain.Station;
import subway.domain.SubwayMap;
import subway.dto.LineStationResponse;
import subway.dto.ShortestPathResponse;
import subway.exception.custom.LineNotExistException;

@Service
public class ShortestPathService {

    private final SubwayMapRepository subwayMapRepository;

    public ShortestPathService(final SubwayMapRepository subwayMapRepository) {
        this.subwayMapRepository = subwayMapRepository;
    }

    public ShortestPathResponse getPath(final String startStation, final String endStation, final int age) {
        final Station start = subwayMapRepository.findStationByName(startStation);
        final Station end = subwayMapRepository.findStationByName(endStation);

        final SubwayMap subwayMap = generateSubwayMap();
        final ShortestPath shortestPath = subwayMap.getShortestPath(start, end);

        final List<LineStation> path = new ArrayList<>(shortestPath.getPath());
        setFirstStationLine(subwayMap, path);

        final List<LineStationResponse> lineStationResponses = mapToLineStationResponse(path);
        final int totalDistance = shortestPath.getDistance();
        final int totalFare = FareCalculator.calculate(findMostExpensiveLine(path), totalDistance, age);

        return new ShortestPathResponse(lineStationResponses, totalDistance, totalFare);
    }

    private SubwayMap generateSubwayMap() {
        final List<Line> lines = subwayMapRepository.findAllLines();
        final List<Sections> lineSections = lines.stream()
            .map(this::findSectionsInLine)
            .collect(Collectors.toList());

        return new SubwayMap(lines, lineSections);
    }

    private Sections findSectionsInLine(final Line line) {
        final List<Section> sectionsInLine = subwayMapRepository.findAllByLineId(line.getId());
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

    private Line findMostExpensiveLine(final List<LineStation> path) {
        return path.stream()
            .map(LineStation::getLine).min(Comparator.comparingInt(Line::getAdditionalFee))
            .orElseThrow(() -> new LineNotExistException("경로가 비어있습니다."));
    }
}
