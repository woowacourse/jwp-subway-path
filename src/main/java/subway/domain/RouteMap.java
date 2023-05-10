package subway.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RouteMap {

    private final List<Station> routeMap;

    public RouteMap(List<Section> sections) {
        this.routeMap = stationsOf(sections);
    }

    private List<Station> stationsOf(List<Section> sections){
        if(sections.isEmpty()){
            return Collections.emptyList();
        }
        return linkStations(sections);
    }

    private List<Station> linkStations(List<Section> sections) {
        List<Station> stations = new ArrayList<>();
        Station targetStation = findFirstStation(sections);
        while (hasNextStation(sections, targetStation) && !isInnerCircle(sections, stations)) {
            stations.add(targetStation);
            targetStation = findNext(sections, targetStation);
        }
        stations.add(targetStation);
        validate(sections, stations);
        return stations;
    }

    private boolean isInnerCircle(List<Section> sections, List<Station> stations) {
        int maxStationSize = sections.size() + 1;
        return maxStationSize < stations.size();
    }

    private void validate(List<Section> sections, List<Station> stations) {
        int maxStationSize = sections.size() + 1;
        if (maxStationSize < stations.size()) {
            throw new IllegalArgumentException("순환노선은 등록할 수 없습니다.");
        }
        if (maxStationSize > stations.size()) {
            throw new IllegalArgumentException("모든 노선이 이어지지 않았습니다.");
        }
    }

    private Station findNext(List<Section> sections, Station targetStation) {
        return sections.stream()
                .filter(section -> section.getLeft().equals(targetStation))
                .map(Section::getRight)
                .findAny()
                .get();
    }

    private Station findFirstStation(List<Section> sections) {
        List<Station> allStartStation = sections.stream()
                .map(Section::getLeft)
                .collect(Collectors.toList());

        List<Station> allEndStations = sections.stream()
                .map(Section::getRight)
                .collect(Collectors.toList());

        allStartStation.removeAll(allEndStations);
        return allStartStation.stream()
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("순환노선은 등록할 수 없습니다."));
    }

    private boolean hasNextStation(List<Section> sections, Station targetStation) {
        return sections.stream()
                .anyMatch(section -> section.getLeft().equals(targetStation));
    }

    public List<Station> getRouteMap() {
        return Collections.unmodifiableList(routeMap);
    }
}
