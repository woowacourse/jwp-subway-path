package subway.domain.path;

import org.jgrapht.graph.DefaultWeightedEdge;
import subway.domain.Line;
import subway.domain.Station;

public final class PathEdgeProxy extends DefaultWeightedEdge {
    private final Line line;
    private final Path path;

    public PathEdgeProxy(final Line line, final Path path) {
        this.line = line;
        this.path = path;
    }

    public static PathEdgeProxy of(final Path path, final Line line) {
        return new PathEdgeProxy(line, path);
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

    public Line getLine() {
        return line;
    }

    public Path toPath() {
        return path;
    }
}
