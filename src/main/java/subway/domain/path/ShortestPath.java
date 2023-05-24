package subway.domain.path;

import subway.domain.Sections;
import subway.domain.Station;

import java.util.List;

public class ShortestPath implements Path {

    private PathFinder pathFinder;

    public ShortestPath(final PathFinder pathFinder) {
        this.pathFinder = pathFinder;
    }

    @Override
    public ShortestPath registerSections(final List<Sections> sections) {
        return new ShortestPath(pathFinder.registerSections(sections));
    }

    @Override
    public List<Station> path(final Station source, final Station target) {
        return pathFinder.findPath(source, target);
    }

    @Override
    public double distance(final Station source, final Station target) {
        return pathFinder.findDistance(source, target);
    }
}
