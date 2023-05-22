package subway.service.domain;

import subway.exception.LineNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Subway {

    private final List<Line> lines;

    public Subway(List<Line> lines) {
        this.lines = lines;
    }

    public List<SingleLine> getAllLine() {
        return lines.stream()
                .map(this::getSingleLine)
                .collect(Collectors.toList());
    }

    public ShortestPath findShortestPath(Station start, Station end, FarePolicies farePolicies, Age age) {
        List<Map<Station, List<Path>>> routeMapInLines = lines.stream()
                .map(line -> line.getLineMap().getMap())
                .collect(Collectors.toList());
        RouteMapInSubway routeMapInSubway = RouteMapInSubway.from(routeMapInLines);
        return routeMapInSubway.getShortestPath(start, end, farePolicies, age);
    }

    public SingleLine getSingleLine(Long lineId) {
        return lines.stream()
                .filter(line -> line.getLineProperty().getId().equals(lineId))
                .map(this::getSingleLine)
                .findFirst()
                .orElseThrow(() -> new LineNotFoundException("조회하려는 노선이 존재하지 않습니다."));
    }

    private SingleLine getSingleLine(Line line) {
        RouteMapInLine lineMap = line.getLineMap();
        return SingleLine.of(line.getLineProperty(), lineMap.getStationsOnLine());
    }

}
