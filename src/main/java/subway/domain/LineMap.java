package subway.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LineMap {

    private final Map<Station, List<Station>> graph;
    private final Map<Station, Boolean> visited;

    public LineMap(final Sections sections) {
        this.graph = initGraph(sections);
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
        for (Station station : graph.keySet()) {
            visited.put(station, false);
        }
        return visited;
    }


    public void addSection(final Section section) {
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();

        List<Station> upStationList = graph.get(upStation);
        List<Station> downStationList = graph.get(downStation);

        upStationList.add(downStation);
        downStationList.add(upStation);
    }

    public List<Station> getEndPointStations() {
        return graph.keySet().stream()
                .filter(key -> graph.get(key).size() == 1)
                .collect(Collectors.toList());
    }

    public List<Station> getOrderedStations(final Station upStationEndPoint) {
        List<Station> stations = new ArrayList<>();
        stations.add(upStationEndPoint);
        dfs(stations, upStationEndPoint);
        return stations;
    }

    private void dfs(final List<Station> stations, final Station station) {
        visited.put(station, true);
        for (Station nextStation : graph.get(station)) {
            if (visited.get(nextStation).equals(Boolean.FALSE)) {
                stations.add(nextStation);
                dfs(stations, nextStation);
            }
        }
    }
}
