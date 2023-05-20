package subway.domain.route;

import java.util.List;
import java.util.stream.IntStream;
import subway.domain.section.Section;
import subway.domain.section.SectionDistance;
import subway.domain.station.Station;

public class ShortestRoute {

    private final List<Station> shortestRoutes;

    public ShortestRoute(final List<Station> shortestRoutes) {
        this.shortestRoutes = shortestRoutes;
    }

    public Distance getShortestDistance(final List<Section> allSections) {
        return IntStream.range(0, shortestRoutes.size() - 1)
            .mapToObj(i -> new Section(shortestRoutes.get(i), shortestRoutes.get(i + 1), SectionDistance.zero()))
            .flatMap(section -> allSections.stream().filter(section::isSameSection))
            .map(section -> new Distance(section.distance().distance()))
            .reduce(new Distance(0), Distance::add);
    }

    public List<Station> getShortestRoutes() {
        return shortestRoutes;
    }
}
