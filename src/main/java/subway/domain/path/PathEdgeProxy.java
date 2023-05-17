package subway.domain.path;

import org.jgrapht.graph.DefaultWeightedEdge;
import subway.domain.Station;

public final class PathEdgeProxy extends DefaultWeightedEdge {
    private final Path path;

    public PathEdgeProxy(final Path path) {
        this.path = path;
    }

    public static PathEdgeProxy from(final Path path) {
        return new PathEdgeProxy(path);
    }

    public Long getId() {
        return path.getId();
    }

    public int getDistance() {
        return path.getDistance();
    }

    @Override
    public double getWeight() {
        return path.getDistance();
    }

    @Override
    public Station getSource() {
        return path.getUp();
    }

    @Override
    public Station getTarget() {
        return path.getDown();
    }

    public Path toPath() {
        return path;
    }
}
