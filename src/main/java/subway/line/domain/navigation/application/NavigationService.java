package subway.line.domain.navigation.application;

import org.springframework.stereotype.Service;
import subway.line.domain.navigation.Navigation;
import subway.line.domain.navigation.SubwayGraph;
import subway.line.domain.navigation.application.exception.NavigationNotFoundException;
import subway.line.domain.section.Section;
import subway.line.domain.section.domain.Distance;
import subway.line.domain.station.Station;

import java.util.List;

@Service
public class NavigationService {
    private static ThreadLocal<Navigation> navigationThreadLocal = null;
    private final SubwayGraph subwayGraph;

    public NavigationService(SubwayGraph subwayGraph) {
        this.subwayGraph = subwayGraph;
    }

    public void updateNavigation(List<List<Section>> sectionsOfAllLine) {
        subwayGraph.initialize(sectionsOfAllLine);
        navigationThreadLocal.set(new Navigation(subwayGraph.makePath()));
    }

    public List<Station> findShortestPath(Station stationA, Station stationB) {
        final var navigation = getNavigation();
        return navigation.findShortestPath(stationA, stationB);
    }

    public Distance findShortestDistance(Station stationA, Station stationB) {
        final var navigation = getNavigation();
        return navigation.findShortestDistance(stationA, stationB);
    }

    private static Navigation getNavigation() {
        if (navigationThreadLocal.get() == null) {
            throw new NavigationNotFoundException();
        }
        return navigationThreadLocal.get();
    }
}
