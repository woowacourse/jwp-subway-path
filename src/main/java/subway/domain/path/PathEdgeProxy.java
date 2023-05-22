package subway.domain.path;

import org.jgrapht.graph.DefaultWeightedEdge;
import subway.domain.Station;

public final class PathEdgeProxy extends DefaultWeightedEdge {
    private final Path path;
    private final int additionalFare;

    public PathEdgeProxy(final Path path, final int additionalFare) {
        this.additionalFare = additionalFare;
        this.path = path;
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

    public int getAdditionalFare() {
        return additionalFare;
    }

    public Path toPath() {
        return path;
    }
}
