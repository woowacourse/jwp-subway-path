package subway.line.domain.navigation;

import org.springframework.stereotype.Service;
import subway.line.domain.navigation.domain.SubwayGraph;
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
        navigationThreadLocal.set(new Navigation(sectionsOfAllLine, subwayGraph));
    }

    public List<Station> findShortestPath(Station stationA, Station stationB) {
        if (navigationThreadLocal.get() == null) {
            throw new IllegalArgumentException("네비게이션 정보가 업데이트되지 않았습니다.");
        }
        return navigationThreadLocal
                .get()
                .findShortestPath(stationA, stationB);
    }

    public Distance findShortestDistance(Station stationA, Station stationB) {
        if (navigationThreadLocal.get() == null) {
            throw new IllegalArgumentException("네비게이션 정보가 업데이트되지 않았습니다.");
        }
        return navigationThreadLocal
                .get()
                .findShortestDistance(stationA, stationB);
    }
}
