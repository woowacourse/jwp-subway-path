package subway.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import subway.exception.notfound.UpStationNotFoundException;

public class LineMap {

    private final Map<Station, List<Station>> lineMap;
    private final Map<Station, Boolean> visited;

    public LineMap(final Sections sections) {
        this.lineMap = initGraph(sections);
        sections.getSections().forEach(this::addSection);
        this.visited = initVisited();
    }

    private Map<Station, List<Station>> initGraph(final Sections sections) {
        Map<Station, List<Station>> graph = new HashMap<>();

        for (Section section : sections.getSections()) {
            graph.put(section.getUpStation(), new ArrayList<>());
            graph.put(section.getDownStation(), new ArrayList<>());
        }

        return graph;
    }

    private Map<Station, Boolean> initVisited() {
        Map<Station, Boolean> visited = new HashMap<>();
        for (Station station : lineMap.keySet()) {
            visited.put(station, false);
        }
        return visited;
    }

    public void addSection(final Section section) {
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();

        List<Station> upStationList = lineMap.get(upStation);
        List<Station> downStationList = lineMap.get(downStation);

        upStationList.add(downStation);
        downStationList.add(upStation);
    }

    public List<Station> getOrderedStations(final Sections sections) {
        List<Station> endPointStations = getEndPointStations();
        Station upStationEndPoint = getUpStationEndPoint(sections, endPointStations);

        List<Station> stations = new ArrayList<>();
        stations.add(upStationEndPoint);
        dfs(stations, upStationEndPoint);
        return stations;
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

    private void dfs(final List<Station> stations, final Station station) {
        visited.put(station, true);
        for (Station nextStation : lineMap.get(station)) {
            if (visited.get(nextStation).equals(Boolean.FALSE)) {
                stations.add(nextStation);
                dfs(stations, nextStation);
            }
        }
    }
}
