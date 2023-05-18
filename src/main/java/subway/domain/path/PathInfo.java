package subway.domain.path;

import java.util.List;

import subway.domain.Sections;
import subway.domain.Station;

public class PathInfo {

    private final List<Station> pathVerticies;
    private final Sections pathEdges;

    public PathInfo(List<Station> pathVerticies, Sections pathEdges) {
        this.pathVerticies = pathVerticies;
        this.pathEdges = pathEdges;
    }

    public List<Station> getPathVerticies() {
        return pathVerticies;
    }

    public Sections getPathEdges() {
        return pathEdges;
    }
}
