package subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public final class PathEdge extends DefaultWeightedEdge {
    private final Path path;

    public PathEdge(final Path path) {
        this.path = path;
    }

    public static PathEdge from(final Path path) {
        return new PathEdge(path);
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
}
