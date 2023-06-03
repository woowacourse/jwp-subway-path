package subway.domain.line;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.exception.notfound.UpStationNotFoundException;

public class LineMap {

    private final Map<String, List<Station>> lineMap;
    private final Map<String, Boolean> visited;
    private final Station upStationEndPoint;

    public LineMap(final Sections sections) {
        this.lineMap = initLineMap(sections);
        sections.getSections().forEach(this::addUndirectedEdgeBySection);
        this.visited = initVisited();
        this.upStationEndPoint = new Station(getUpStationEndPoint(sections, getEndPointStations()));
    }

    private Map<String, List<Station>> initLineMap(final Sections sections) {
        Map<String, List<Station>> lineMap = new HashMap<>();

        for (Section section : sections.getSections()) {
            lineMap.put(section.getUpStation().getName(), new ArrayList<>());
            lineMap.put(section.getDownStation().getName(), new ArrayList<>());
        }

        return lineMap;
    }

    private void addUndirectedEdgeBySection(final Section section) {
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();

        List<Station> upStationList = lineMap.get(upStation.getName());
        List<Station> downStationList = lineMap.get(downStation.getName());

        upStationList.add(downStation);
        downStationList.add(upStation);
    }

    private Map<String, Boolean> initVisited() {
        Map<String, Boolean> visited = new HashMap<>();
        for (String station : lineMap.keySet()) {
            visited.put(station, false);
        }
        return visited;
    }

    private String getUpStationEndPoint(final Sections sections, final List<String> endPointStations) {
        return sections.getSections().stream()
                .flatMap(section -> endPointStations.stream()
                        .filter(station -> station.equals(section.getUpStation().getName())))
                .findFirst()
                .orElseThrow(UpStationNotFoundException::new);
    }

    private List<String> getEndPointStations() {
        return lineMap.keySet().stream()
                .filter(key -> lineMap.get(key).size() == 1)
                .collect(Collectors.toList());
    }

    public List<Station> getOrderedStations() {
        List<Station> stations = new ArrayList<>();
        stations.add(upStationEndPoint);
        dfs(stations, upStationEndPoint);
        return stations;
    }

    private void dfs(final List<Station> stations, final Station station) {
        visited.put(station.getName(), true);
        for (Station nextStation : lineMap.get(station.getName())) {
            if (visited.get(nextStation.getName()).equals(Boolean.FALSE)) {
                stations.add(nextStation);
                dfs(stations, nextStation);
            }
        }
    }
}
