package subway.application.core.domain;

import subway.application.core.exception.CircularRouteException;
import subway.application.core.exception.RouteNotConnectedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RouteMap {

    private final List<Station> routeMap;

    public RouteMap(List<Section> sections) {
        this.routeMap = routeOf(sections);
    }

    private List<Station> routeOf(List<Section> sections) {
        if (sections.isEmpty()) {
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
        List<Station> endPoints = getEndPoints(sections);

        return endPoints.stream()
                .findAny()
                .orElseThrow(CircularRouteException::new);
    }

    private List<Station> getEndPoints(List<Section> sections) {
        List<Station> allUpBounds = sections.stream()
                .map(Section::getUpBound)
                .collect(Collectors.toList());

        List<Station> allDownBounds = sections.stream()
                .map(Section::getDownBound)
                .collect(Collectors.toList());

        allUpBounds.removeAll(allDownBounds);
        return allUpBounds;
    }

    private boolean hasNextStation(List<Section> sections, Station targetStation) {
        return sections.stream()
                .anyMatch(section -> section.getUpBound().equals(targetStation));
    }

    private boolean isInnerCircle(List<Section> sections, List<Station> stations) {
        int maxStationSize = sections.size() + 1;
        return maxStationSize < stations.size();
    }

    private Station findNext(List<Section> sections, Station targetStation) {
        return sections.stream()
                .filter(section -> section.getUpBound().equals(targetStation))
                .map(Section::getDownBound)
                .findAny()
                .orElseThrow();
    }

    private void validate(List<Section> sections, List<Station> stations) {
        int maxStationSize = sections.size() + 1;
        if (maxStationSize < stations.size()) {
            throw new CircularRouteException();
        }
        if (maxStationSize > stations.size()) {
            throw new RouteNotConnectedException();
        }
    }

    public List<Station> value() {
        return Collections.unmodifiableList(routeMap);
    }
}
