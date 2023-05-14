package subway.application.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RouteMap {

    private final List<Station> routeMap;

    public RouteMap(List<Section> sections) {
        this.routeMap = routeOf(sections);
    }

    private List<Station> routeOf(List<Section> sections){
        if(sections.isEmpty()){
            return Collections.emptyList();
        }
        return linkStations(sections);
    }

    private List<Station> linkStations(List<Section> sections) {
        List<Station> stations = new ArrayList<>();
        Station indexStation = findFirstStation(sections);
        while (hasNextStation(sections, indexStation) && !isInnerCircle(sections, stations)) {
            stations.add(indexStation);
            indexStation = findNext(sections, indexStation);
        }
        stations.add(indexStation);
        validate(sections, stations);
        return stations;
    }

    private Station findFirstStation(List<Section> sections) {
        List<Station> allUpStations = sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());
        List<Station> allDownStations = sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());

        allUpStations.removeAll(allDownStations);

        return allUpStations.stream()
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("순환노선은 등록할 수 없습니다."));
    }

    private boolean hasNextStation(List<Section> sections, Station targetStation) {
        return sections.stream()
                .anyMatch(section -> section.getUpStation().equals(targetStation));
    }

    private boolean isInnerCircle(List<Section> sections, List<Station> stations) {
        int maxStationSize = sections.size() + 1;
        return maxStationSize < stations.size();
    }

    private Station findNext(List<Section> sections, Station targetStation) {
        return sections.stream()
                .filter(section -> section.getUpStation().equals(targetStation))
                .map(Section::getDownStation)
                .findAny()
                .orElseThrow();
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

    public List<Station> getRouteMap() {
        return Collections.unmodifiableList(routeMap);
    }
}
