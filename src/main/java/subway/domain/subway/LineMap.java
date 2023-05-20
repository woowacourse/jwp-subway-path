package subway.domain.subway;

import subway.exception.UpStationNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

public class LineMap {

    private final Map<Station, List<Station>> lineMap;

    private LineMap(final Map<Station, List<Station>> lineMap) {
        this.lineMap = lineMap;
    }

    public static LineMap from(final Sections sections) {
        Map<Station, List<Station>> lineMap = new HashMap<>();

        sections.getSections()
                .forEach(section -> {
                    Station upStation = section.getUpStation();
                    Station downStation = section.getDownStation();

                    lineMap.computeIfAbsent(upStation, station -> new ArrayList<>()).add(downStation);
                    lineMap.computeIfAbsent(downStation, station -> new ArrayList<>()).add(upStation);
                });

        return new LineMap(lineMap);
    }

    public List<Station> getOrderedStations(final Sections sections) {
        List<Station> endPointStations = getEndPointStations();
        Station upStationEndPoint = getUpStationEndPoint(sections, endPointStations);

        return bfs(upStationEndPoint);
    }

    private List<Station> bfs(final Station upStationEndPoint) {
        Map<Station, Boolean> visited = initVisited();
        List<Station> stations = new ArrayList<>();

        Queue<Station> queue = new LinkedList<>();
        queue.add(upStationEndPoint);

        while (!queue.isEmpty()) {
            Station station = queue.poll();
            stations.add(station);
            visited.put(station, true);
            addNextStation(visited, queue, station);
        }
        return stations;
    }

    private void addNextStation(final Map<Station, Boolean> visited, final Queue<Station> queue, final Station station) {
        for (Station nextStation : lineMap.get(station)) {
            addNotVisitedStation(visited, queue, nextStation);
        }
    }

    private void addNotVisitedStation(final Map<Station, Boolean> visited, final Queue<Station> queue, final Station nextStation) {
        if (!visited.get(nextStation)) {
            queue.add(nextStation);
        }
    }

    private Map<Station, Boolean> initVisited() {
        Map<Station, Boolean> visited = new HashMap<>();
        for (Station station : lineMap.keySet()) {
            visited.put(station, false);
        }
        return visited;
    }

    private List<Station> getEndPointStations() {
        return lineMap.keySet().stream()
                .filter(key -> lineMap.get(key).size() == 1)
                .collect(Collectors.toList());
    }

    private Station getUpStationEndPoint(final Sections sections, final List<Station> endPointStations) {
        return sections.getSections().stream()
                .flatMap(section -> endPointStations.stream()
                        .filter(station -> station.equals(section.getUpStation())))
                .findFirst()
                .orElseThrow(UpStationNotFoundException::new);
    }
}
